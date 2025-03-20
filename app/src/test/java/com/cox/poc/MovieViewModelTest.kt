package com.cox.poc

import com.cox.domain.model.ResultResponse
import com.cox.domain.usecase.GetUpcomingMoviesUseCase
import com.cox.domain.util.DomainErrorFactory
import com.cox.domain.util.Either
import com.cox.poc.ui.viewmodel.MovieViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

/**
 * class of [MovieViewModel]
 */

@ExperimentalCoroutinesApi
class MovieViewModelTest {

    private val mockUseCase: GetUpcomingMoviesUseCase = mockk()

    private lateinit var movieViewModel: MovieViewModel
    private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        movieViewModel = MovieViewModel(mockUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getUpcoming should update state with movies when useCase call is successful`() =
        runTest {
            val page = 1
            val movies =
                listOf(ResultResponse(1, "Backdrop", "2023-10-01", "en", "Title", "Overview", 8.5))
            coEvery { mockUseCase.invoke(page) } returns Either.Right(movies)

            movieViewModel.getUpcoming()

            val state = movieViewModel.upcomingState.first()
            assertEquals(movies, state.data)
            assertEquals(false, state.failed)
            assertEquals(null, state.errorMessage)
        }

    @Test
    fun `getUpcoming should update state with error message when useCase call fails`() =
        runTest {
            val page = 1
            val error = DomainErrorFactory.fromHttpCode(404)
            coEvery { mockUseCase.invoke(page) } returns Either.Left(error)

            movieViewModel.getUpcoming()

            val state = movieViewModel.upcomingState.first()
            assertEquals(true, state.failed)
            assertEquals(error.userFriendlyMessage, state.errorMessage)
        }
}
