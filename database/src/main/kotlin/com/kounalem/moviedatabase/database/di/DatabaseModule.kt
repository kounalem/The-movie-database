package com.kounalem.moviedatabase.database.di

import android.app.Application
import androidx.room.Room
import com.kounalem.moviedatabase.database.movie.AppDatabase
import com.kounalem.moviedatabase.database.movie.LocalDataSource
import com.kounalem.moviedatabase.database.movie.LocalDataSourceImpl
import com.kounalem.moviedatabase.database.movie.MovieDao
import com.kounalem.moviedatabase.database.movie.TvShowsDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    internal fun provideLocalDatabase(app: Application): AppDatabase {
        return Room.databaseBuilder(
            app, AppDatabase::class.java, AppDatabase.NAME
        ).build()
    }

    @Provides
    @Singleton
    internal fun provideMovieDao(localDatabase: AppDatabase): MovieDao {
        return localDatabase.movieDao
    }

    @Provides
    @Singleton
    internal fun provideTvShowDao(localDatabase: AppDatabase): TvShowsDao {
        return localDatabase.tvShowDao
    }

    @Provides
    @Singleton
    internal fun provideLocalDataSource(
        movieDataSource: MovieDao,
        tvShowsDataSource: TvShowsDao,
    ): LocalDataSource =
        LocalDataSourceImpl(daoMovies = movieDataSource, daoTvShows = tvShowsDataSource)

}