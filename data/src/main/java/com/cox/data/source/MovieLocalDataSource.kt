package com.cox.data.source

import com.cox.data.local.MovieEntity
import com.cox.domain.util.DomainErrorFactory
import com.cox.domain.util.Either

interface IMovieLocalDataSource {
    suspend fun saveUpcomingMovies(movies: List<MovieEntity>): Either<DomainErrorFactory, Unit>
    suspend fun getUpcomingMovies(): Either<DomainErrorFactory, List<MovieEntity>>
}
