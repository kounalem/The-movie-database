package com.kounalem.moviedatabase.core.data.di

import com.kounalem.moviedatabase.core.data.movie.MovieRepository
import com.kounalem.moviedatabase.core.data.movie.MovieRepositoryImpl
import com.kounalem.moviedatabase.database.movie.LocalDataSource
import com.kounalem.moviedatabase.network.movies.ServerDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CoreDataModule {

    @Provides
    @Singleton
    fun provideMovieRepository(
        serverDataSource: ServerDataSource,
        localDataSource: LocalDataSource,
    ): MovieRepository {
        return MovieRepositoryImpl(
            server = serverDataSource,
            local = localDataSource,
            coroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
        )
    }

}