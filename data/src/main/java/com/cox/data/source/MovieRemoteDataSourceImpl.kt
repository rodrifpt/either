package com.cox.data.source

import com.cox.data.remote.IMovieApi
import com.cox.domain.model.UpcomingResponse
import com.cox.domain.util.DomainErrorFactory
import com.cox.domain.util.Either
import javax.inject.Inject

class MovieRemoteDataSourceImpl @Inject constructor(
    private val movieService: IMovieApi
) : IMovieRemoteDataSource {
    override suspend fun getUpcomingMovies(page: Int): Either<DomainErrorFactory, UpcomingResponse> {
        return try {
            val response = movieService.getUpcoming(page = page)
            if (response.isSuccessful) Either.Right(checkNotNull(response.body()))
             else Either.Left(DomainErrorFactory.fromHttpCode(response.code()))
        } catch (exception: Exception) {
            Either.Left(DomainErrorFactory.fromException(exception))
        }
    }
}
