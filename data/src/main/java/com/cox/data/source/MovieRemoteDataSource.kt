package com.cox.data.source

import com.cox.domain.model.UpcomingResponse
import com.cox.domain.util.DomainErrorFactory
import com.cox.domain.util.Either

interface IMovieRemoteDataSource {
    suspend fun getUpcomingMovies(page: Int): Either<DomainErrorFactory, UpcomingResponse>
}
