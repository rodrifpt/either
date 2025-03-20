package com.cox.data.di

import com.cox.data.repositoryimpl.MovieRepositoryImpl
import com.cox.data.service.IMovieService
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
    fun provideMovieService(retrofit: Retrofit): IMovieService {
        return retrofit.create(IMovieService::class.java)
    }

    @Provides
    @Singleton
    fun provideMovieRepository(service: IMovieService): IMovieRepository {
        return MovieRepositoryImpl(service)
    }
}
