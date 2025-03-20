package com.cox.data.repositoryimpl

import com.cox.data.local.MovieEntity
import com.cox.data.mapper.toDomain
import com.cox.data.source.IMovieLocalDataSource
import com.cox.data.source.IMovieRemoteDataSource
import com.cox.domain.model.ResultResponse
import com.cox.domain.model.UpcomingResponse
import com.cox.domain.util.DomainErrorFactory
import com.cox.domain.util.Either
import com.cox.domain.util.ErrorType
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

/**
 * class of [MovieRepositoryImpl]
 */

class MovieRepositoryImplTest {

    private val mockRemoteDataSource: IMovieRemoteDataSource = mockk()
    private val mockLocalDataSource: IMovieLocalDataSource = mockk()

    private lateinit var movieRepository: MovieRepositoryImpl

    @Before
    fun setUp() {
        movieRepository = MovieRepositoryImpl(mockRemoteDataSource, mockLocalDataSource)
    }

    @Test
    fun `getUpcoming should return businessError when page is less than or equal to 0`() =
        runBlocking {
            val page = 0
            val result = movieRepository.getUpcoming(page)

            assert(result is Either.Left)
            val error = (result as Either.Left).value
            assertEquals(ErrorType.BusinessError, error.errorType)
            assertEquals("Page number must be greater than 0.", error.userFriendlyMessage)
        }

    @Test
    fun `getUpcoming should return Either Right with remote data when API call is successful`() =
        runBlocking {
            val page = 1
            val remoteData = UpcomingResponse(
                results = listOf(
                    ResultResponse(
                        1,
                        "Backdrop",
                        "2023-10-01",
                        "en",
                        "Title",
                        "Overview",
                        8.5
                    )
                )
            )
            coEvery { mockRemoteDataSource.getUpcomingMovies(page) } returns Either.Right(remoteData)
            coEvery { mockLocalDataSource.saveUpcomingMovies(any()) } returns Either.Right(Unit)

            val result = movieRepository.getUpcoming(page)

            assertEquals(Either.Right(remoteData.results), result)
            coVerify(exactly = 1) { mockRemoteDataSource.getUpcomingMovies(page) }
            coVerify(exactly = 1) { mockLocalDataSource.saveUpcomingMovies(any()) }
        }

    @Test
    fun `getUpcoming should return Either Left with database error when saving to local database fails`() =
        runBlocking {
            val page = 1
            val remoteData = UpcomingResponse(
                results = listOf(
                    ResultResponse(
                        1,
                        "Backdrop",
                        "2023-10-01",
                        "en",
                        "Title",
                        "Overview",
                        8.5
                    )
                )
            )
            val databaseError = DomainErrorFactory(
                errorCode = -1,
                errorType = ErrorType.DatabaseError,
                userFriendlyMessage = "Error saving data to the database."
            )
            coEvery { mockRemoteDataSource.getUpcomingMovies(page) } returns Either.Right(remoteData)
            coEvery { mockLocalDataSource.saveUpcomingMovies(any()) } returns Either.Left(
                databaseError
            )

            val result = movieRepository.getUpcoming(page)

            assertEquals(Either.Left(databaseError), result)
            coVerify(exactly = 1) { mockRemoteDataSource.getUpcomingMovies(page) }
            coVerify(exactly = 1) { mockLocalDataSource.saveUpcomingMovies(any()) }
        }

    @Test
    fun `getUpcoming should return Either Right with local data when API call fails but local data exists`() =
        runBlocking {
            val page = 1
            val apiError = DomainErrorFactory.fromHttpCode(404)
            val localData =
                listOf(MovieEntity(1, "Backdrop", "2023-10-01", "en", "Title", "Overview", 8.5))
            coEvery { mockRemoteDataSource.getUpcomingMovies(page) } returns Either.Left(apiError)
            coEvery { mockLocalDataSource.getUpcomingMovies() } returns Either.Right(localData)

            val result = movieRepository.getUpcoming(page)

            assertEquals(Either.Right(localData.map { it.toDomain() }), result)
            coVerify(exactly = 1) { mockRemoteDataSource.getUpcomingMovies(page) }
            coVerify(exactly = 1) { mockLocalDataSource.getUpcomingMovies() }
        }

    @Test
    fun `getUpcoming should return Either Left with API error when both API and local database fail`() =
        runBlocking {

            val page = 1
            val apiError = DomainErrorFactory.fromHttpCode(404)
            val databaseError = DomainErrorFactory(
                errorCode = -1,
                errorType = ErrorType.DatabaseError,
                userFriendlyMessage = "Error loading data from the database."
            )
            coEvery { mockRemoteDataSource.getUpcomingMovies(page) } returns Either.Left(apiError)
            coEvery { mockLocalDataSource.getUpcomingMovies() } returns Either.Left(databaseError)

            val result = movieRepository.getUpcoming(page)

            assertEquals(Either.Left(apiError), result)
            coVerify(exactly = 1) { mockRemoteDataSource.getUpcomingMovies(page) }
            coVerify(exactly = 1) { mockLocalDataSource.getUpcomingMovies() }
        }
}
