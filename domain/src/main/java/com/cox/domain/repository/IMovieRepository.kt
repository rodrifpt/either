package com.cox.domain.repository

import com.cox.domain.model.ResultResponse
import com.cox.domain.util.DomainErrorFactory
import com.cox.domain.util.Either

interface IMovieRepository {
    suspend fun getUpcoming(page: Int): Either<DomainErrorFactory, List<ResultResponse>>
}
