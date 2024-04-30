package com.kounalem.moviedatabase.core.data.di

import android.content.Context
import com.kounalem.moviedatabase.core.data.ConnectivityManagerNetworkMonitor
import com.kounalem.moviedatabase.core.data.NetworkMonitor
import com.kounalem.moviedatabase.core.data.movie.MovieRepositoryImpl
import com.kounalem.moviedatabase.core.data.series.TvShowRepositoryImpl
import com.kounalem.moviedatabase.database.movie.LocalDataSource
import com.kounalem.moviedatabase.network.movies.MoviesDataSource
import com.kounalem.moviedatabase.network.series.SeriesDataSource
import com.kounalem.moviedatabase.repository.MovieRepository
import com.kounalem.moviedatabase.repository.TvShowRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CoreDataModule {
    @Provides
    @Singleton
    fun provideMovieRepository(
        serverDataSource: MoviesDataSource,
        localDataSource: LocalDataSource,
        @com.kounalem.moviedatabase.shared.annotation.Application appScope: CoroutineScope,
    ): MovieRepository {
        return MovieRepositoryImpl(
            server = serverDataSource,
            local = localDataSource,
            coroutineScope = appScope,
        )
    }

    @Provides
    @Singleton
    fun provideShowRepository(
        serverDataSource: SeriesDataSource,
        localDataSource: LocalDataSource,
        @com.kounalem.moviedatabase.shared.annotation.Application appScope: CoroutineScope,
    ): TvShowRepository {
        return TvShowRepositoryImpl(
            server = serverDataSource,
            local = localDataSource,
            coroutineScope = appScope,
        )
    }

    @Provides
    @Singleton
    fun provideNetworkMonitor(
        @ApplicationContext context: Context,
    ): NetworkMonitor = ConnectivityManagerNetworkMonitor(context)
}
