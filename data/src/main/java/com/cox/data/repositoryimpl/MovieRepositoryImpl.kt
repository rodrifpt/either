package com.cox.data.repositoryimpl

import com.cox.data.service.IMovieService
import com.cox.domain.model.UpcomingResponse
import com.cox.domain.repository.IMovieRepository
import com.cox.domain.util.DomainErrorFactory
import com.cox.domain.util.Either
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(private val service: IMovieService) :
    IMovieRepository {
    override suspend fun getUpcoming(page: Int): Either<DomainErrorFactory, UpcomingResponse> =
        try {
            val response = service.getUpcoming(page = page)
            when {
                response.isSuccessful -> Either.Right(checkNotNull(response.body()))
                else -> Either.Left(DomainErrorFactory.fromHttpCode(response.code()))
            }
        } catch (exception: Exception) {
            Either.Left(DomainErrorFactory.fromException(exception))
        }
}
