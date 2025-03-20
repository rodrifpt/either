package com.cox.domain.usecase

import com.cox.domain.model.ResultResponse
import com.cox.domain.repository.IMovieRepository
import com.cox.domain.util.DomainErrorFactory
import com.cox.domain.util.Either
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

/**
 * class of [GetUpcomingMoviesUseCase]
 */
class GetUpcomingMoviesUseCaseTest {

    private val mockRepository: IMovieRepository = mockk()

    private lateinit var getUpcomingMoviesUseCase: GetUpcomingMoviesUseCase

    @Before
    fun setUp() {
        getUpcomingMoviesUseCase = GetUpcomingMoviesUseCase(mockRepository)
    }

    @Test
    fun `invoke should return Either Right with movies when repository call is successful`() =
        runBlocking {
            val page = 1
            val movies =
                listOf(ResultResponse(1, "Backdrop", "2023-10-01", "en", "Title", "Overview", 8.5))
            coEvery { mockRepository.getUpcoming(page) } returns Either.Right(movies)

            val result = getUpcomingMoviesUseCase(page)

            assertEquals(Either.Right(movies), result)
            coVerify(exactly = 1) { mockRepository.getUpcoming(page) }
        }

    @Test
    fun `invoke should return Either Left with error when repository call fails`() = runBlocking {
        val page = 1
        val error = DomainErrorFactory.fromHttpCode(404)
        coEvery { mockRepository.getUpcoming(page) } returns Either.Left(error)

        val result = getUpcomingMoviesUseCase(page)

        assertEquals(Either.Left(error), result)
        coVerify(exactly = 1) { mockRepository.getUpcoming(page) }
    }
}
