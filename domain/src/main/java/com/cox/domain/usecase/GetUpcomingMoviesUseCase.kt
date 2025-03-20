package com.cox.domain.usecase

import com.cox.domain.model.ResultResponse
import com.cox.domain.repository.IMovieRepository
import com.cox.domain.util.DomainErrorFactory
import com.cox.domain.util.Either

/**
 * Test class of [com.cox.domain.usecase.GetUpcomingMoviesUseCase]
 */
class GetUpcomingMoviesUseCase(
    private val repository: IMovieRepository
) {
    suspend operator fun invoke(page: Int): Either<DomainErrorFactory, List<ResultResponse>> {
        return repository.getUpcoming(page)
    }
}
