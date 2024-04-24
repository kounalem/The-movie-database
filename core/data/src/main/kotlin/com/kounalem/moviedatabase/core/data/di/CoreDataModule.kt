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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CoreDataModule {
    @Provides
    @Singleton
    fun provideMovieRepository(
        serverDataSource: MoviesDataSource,
        localDataSource: LocalDataSource,
    ): MovieRepository {
        return MovieRepositoryImpl(
            server = serverDataSource,
            local = localDataSource,
            coroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
        )
    }

    @Provides
    @Singleton
    fun provideShowRepository(
        serverDataSource: SeriesDataSource,
        localDataSource: LocalDataSource,
    ): TvShowRepository {
        return TvShowRepositoryImpl(
            server = serverDataSource,
            local = localDataSource,
            coroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
        )
    }

    @Provides
    @Singleton
    fun provideNetworkMonitor(
        @ApplicationContext context: Context,
    ): NetworkMonitor = ConnectivityManagerNetworkMonitor(context)
}
