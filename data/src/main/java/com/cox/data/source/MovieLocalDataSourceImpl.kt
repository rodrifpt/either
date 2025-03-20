package com.cox.data.source

import com.cox.data.local.IMovieDao
import com.cox.data.local.MovieEntity
import com.cox.domain.util.DomainErrorFactory
import com.cox.domain.util.Either
import com.cox.domain.util.ErrorType
import javax.inject.Inject

class MovieLocalDataSourceImpl @Inject constructor(
    private val movieDao: IMovieDao
) : IMovieLocalDataSource {

    override suspend fun saveUpcomingMovies(movies: List<MovieEntity>): Either<DomainErrorFactory, Unit> {
        return try {
            movieDao.transactionUpcoming(movies)
            Either.Right(Unit)
        } catch (exception: Exception) {
            Either.Left(
                DomainErrorFactory(
                    errorCode = exception.hashCode(),
                    errorType = ErrorType.DatabaseError,
                    userFriendlyMessage = "Error saving data to the database.",
                    developerMessage = "Database Error: ${exception.message}"
                )
            )
        }
    }

    override suspend fun getUpcomingMovies(): Either<DomainErrorFactory, List<MovieEntity>> {
        return try {
            Either.Right(movieDao.getUpcomingMovies())
        } catch (exception: Exception) {
            Either.Left(
                DomainErrorFactory(
                    errorCode = exception.hashCode(),
                    errorType = ErrorType.DatabaseError,
                    userFriendlyMessage = "Error loading data from the database.",
                    developerMessage = "Database Error: ${exception.message}"
                )
            )
        }
    }
}
