package com.cox.data.source

import com.cox.data.remote.IMovieApi
import com.cox.domain.model.UpcomingResponse
import com.cox.domain.util.Either
import com.cox.domain.util.ErrorType
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import java.io.IOException

/**
 * class of [MovieRemoteDataSourceImpl]
 */

class MovieRemoteDataSourceImplTest {

    private val mockMovieService: IMovieApi = mockk()

    private lateinit var movieRemoteDataSource: MovieRemoteDataSourceImpl

    @Before
    fun setUp() {
        movieRemoteDataSource = MovieRemoteDataSourceImpl(mockMovieService)
    }

    @Test
    fun `getUpcomingMovies should return Either Right when API call is successful`() = runBlocking {
        val page = 1
        val upcomingResponse = UpcomingResponse(results = emptyList())
        val response = Response.success(upcomingResponse)
        coEvery { mockMovieService.getUpcoming(page = page) } returns response

        val result = movieRemoteDataSource.getUpcomingMovies(page)

        assertEquals(Either.Right(upcomingResponse), result)
    }

    @Test
    fun `getUpcomingMovies should return Either Left with ServerError when API call fails with 404`() =
        runBlocking {
            val page = 1
            val response = Response.error<UpcomingResponse>(404, mockk(relaxed = true))
            coEvery { mockMovieService.getUpcoming(page = page) } returns response

            val result = movieRemoteDataSource.getUpcomingMovies(page)

            assert(result is Either.Left)
            val error = (result as Either.Left).value
            assertEquals(ErrorType.ServerError, error.errorType)
            assertEquals("Request error. Please check your input.", error.userFriendlyMessage)
            assertEquals("HTTP Error: 404", error.developerMessage)
        }

    @Test
    fun `getUpcomingMovies should return Either Left with ServerError when API call fails with 500`() =
        runBlocking {
            val page = 1
            val response = Response.error<UpcomingResponse>(500, mockk(relaxed = true))
            coEvery { mockMovieService.getUpcoming(page = page) } returns response

            val result = movieRemoteDataSource.getUpcomingMovies(page)

            assert(result is Either.Left)
            val error = (result as Either.Left).value
            assertEquals(ErrorType.ServerError, error.errorType)
            assertEquals("Server error. Please try again later.", error.userFriendlyMessage)
            assertEquals("Server Error: 500", error.developerMessage)
        }

    @Test
    fun `getUpcomingMovies should return Either Left with NetworkError when API call throws IOException`() =
        runBlocking {
            val page = 1
            val exception = IOException("Network error")
            coEvery { mockMovieService.getUpcoming(page = page) } throws exception

            val result = movieRemoteDataSource.getUpcomingMovies(page)

            assert(result is Either.Left)
            val error = (result as Either.Left).value
            assertEquals(ErrorType.NetworkError, error.errorType)
            assertEquals(
                "Connection error. Please check your internet connection.",
                error.userFriendlyMessage
            )
            assertEquals("Exception: Network error", error.developerMessage)
        }
}
