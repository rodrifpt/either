package com.cox.data.di

import com.cox.data.local.IMovieDao
import com.cox.data.remote.IMovieApi
import com.cox.data.repositoryimpl.MovieRepositoryImpl
import com.cox.data.source.IMovieLocalDataSource
import com.cox.data.source.IMovieRemoteDataSource
import com.cox.data.source.MovieLocalDataSourceImpl
import com.cox.data.source.MovieRemoteDataSourceImpl
import com.cox.domain.repository.IMovieRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    @Provides
    @Singleton
    fun provideMovieService(retrofit: Retrofit): IMovieApi = retrofit.create(IMovieApi::class.java)

    @Provides
    @Singleton
    fun provideMovieRemoteDataSource(service: IMovieApi): IMovieRemoteDataSource =
        MovieRemoteDataSourceImpl(service)

    @Provides
    @Singleton
    fun provideMovieLocalDataSource(dao: IMovieDao): IMovieLocalDataSource =
        MovieLocalDataSourceImpl(dao)

    @Provides
    @Singleton
    fun provideMovieRepository(
        remoteDataSource: IMovieRemoteDataSource,
        localDataSource: IMovieLocalDataSource
    ): IMovieRepository = MovieRepositoryImpl(remoteDataSource, localDataSource)

}
