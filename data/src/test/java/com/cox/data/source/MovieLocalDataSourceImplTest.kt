package com.cox.data.source

import com.cox.data.local.IMovieDao
import com.cox.data.local.MovieEntity
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
 * class of [MovieLocalDataSourceImpl]
 */

class MovieLocalDataSourceImplTest {

    private val mockMovieDao: IMovieDao = mockk()
    private lateinit var movieLocalDataSource: MovieLocalDataSourceImpl

    @Before
    fun setUp() {
        movieLocalDataSource = MovieLocalDataSourceImpl(mockMovieDao)
    }

    @Test
    fun `saveUpcomingMovies should return Either Right when transaction succeeds`() = runBlocking {
        val movies = listOf(MovieEntity(1, "Backdrop", "2023-10-01", "en", "Title", "Overview", 8.5))
        coEvery { mockMovieDao.transactionUpcoming(movies) } returns Unit

        val result = movieLocalDataSource.saveUpcomingMovies(movies)

        assertEquals(Either.Right(Unit), result)
        coVerify(exactly = 1) { mockMovieDao.transactionUpcoming(movies) }
    }

    @Test
    fun `saveUpcomingMovies should return Either Left with DatabaseError when transaction fails`() = runBlocking {
        val movies = listOf(MovieEntity(1, "Backdrop", "2023-10-01", "en", "Title", "Overview", 8.5))
        val exception = Exception("Database error")
        coEvery { mockMovieDao.transactionUpcoming(movies) } throws exception

        val result = movieLocalDataSource.saveUpcomingMovies(movies)

        assert(result is Either.Left)
        val error = (result as Either.Left).value
        assertEquals(ErrorType.DatabaseError, error.errorType)
        assertEquals("Error saving data to the database.", error.userFriendlyMessage)
        assertEquals("Database Error: ${exception.message}", error.developerMessage)
        coVerify(exactly = 1) { mockMovieDao.transactionUpcoming(movies) }
    }

    @Test
    fun `getUpcomingMovies should return Either Right with movies when query succeeds`() = runBlocking {
        val movies = listOf(MovieEntity(1, "Backdrop", "2023-10-01", "en", "Title", "Overview", 8.5))
        coEvery { mockMovieDao.getUpcomingMovies() } returns movies

        val result = movieLocalDataSource.getUpcomingMovies()

        assertEquals(Either.Right(movies), result)
        coVerify(exactly = 1) { mockMovieDao.getUpcomingMovies() }
    }

    @Test
    fun `getUpcomingMovies should return Either Left with DatabaseError when query fails`() = runBlocking {
        val exception = Exception("Database error")
        coEvery { mockMovieDao.getUpcomingMovies() } throws exception

        val result = movieLocalDataSource.getUpcomingMovies()

        assert(result is Either.Left)
        val error = (result as Either.Left).value
        assertEquals(ErrorType.DatabaseError, error.errorType)
        assertEquals("Error loading data from the database.", error.userFriendlyMessage)
        assertEquals("Database Error: ${exception.message}", error.developerMessage)
        coVerify(exactly = 1) { mockMovieDao.getUpcomingMovies() }
    }
}
