package com.cox.data.repositoryimpl

import com.cox.data.mapper.toDomain
import com.cox.data.mapper.toEntity
import com.cox.data.source.IMovieLocalDataSource
import com.cox.data.source.IMovieRemoteDataSource
import com.cox.domain.model.ResultResponse
import com.cox.domain.repository.IMovieRepository
import com.cox.domain.util.DomainErrorFactory
import com.cox.domain.util.Either
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val remoteDataSource: IMovieRemoteDataSource,
    private val localDataSource: IMovieLocalDataSource
) : IMovieRepository {

    override suspend fun getUpcoming(page: Int): Either<DomainErrorFactory, List<ResultResponse>> {

        if (page <= 0) {
            return Either.Left(
                DomainErrorFactory.businessError("Page number must be greater than 0.")
            )
        }

        return when (val remoteResult = remoteDataSource.getUpcomingMovies(page)) {
            is Either.Right -> {
                // Save the data to the local database
                val movies = remoteResult.value.results.map { it.toEntity() }
                when (val saveResult = localDataSource.saveUpcomingMovies(movies)) {
                    is Either.Right -> Either.Right(remoteResult.value.results)
                    is Either.Left -> saveResult // Return the database error
                }
            }
            is Either.Left -> {
                // If there's an API error, try to get the data from the local database
                when (val localResult = localDataSource.getUpcomingMovies()) {
                    is Either.Right -> {
                        if (localResult.value.isNotEmpty())
                            Either.Right(localResult.value.map { it.toDomain() })
                         else
                            remoteResult // Return the API error if there's no local data
                    }
                    is Either.Left -> localResult // Return the database error
                }
            }
        }
    }
}
