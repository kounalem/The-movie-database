package com.kounalem.moviedatabase.di

import android.app.Application
import androidx.room.Room
import com.kounalem.moviedatabase.data.MovieRepositoryImpl
import com.kounalem.moviedatabase.data.db.AppDatabase
import com.kounalem.moviedatabase.data.db.LocalDataSource
import com.kounalem.moviedatabase.data.db.MovieDao
import com.kounalem.moviedatabase.data.mappers.MovieDataMapper
import com.kounalem.moviedatabase.data.mappers.MovieDescriptionDataMapper
import com.kounalem.moviedatabase.data.mappers.PopularMoviesDataMapper
import com.kounalem.moviedatabase.data.remote.MoviesApiService
import com.kounalem.moviedatabase.data.remote.ServerDataSource
import com.kounalem.moviedatabase.domain.MovieRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideLocalDatabase(app: Application): AppDatabase {
        return Room.databaseBuilder(
            app,
            AppDatabase::class.java,
            AppDatabase.NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideMovieApi(): MoviesApiService {
        return Retrofit.Builder()
            .baseUrl("http://api.themoviedb.org/3/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BASIC
                })
                .addInterceptor(RequestInterceptor()).build()
            )
            .build()
            .create()
    }


    @Provides
    @Singleton
    fun bindMovieRepository(
        serverDataSource: ServerDataSource,
        localDataSource: LocalDataSource,
        movieDescriptionDataMapper: MovieDescriptionDataMapper,
        movieDataMapper: MovieDataMapper,
        popularMoviesDataMapper: PopularMoviesDataMapper,
    ): MovieRepository {
        return MovieRepositoryImpl(
            serverDataSource = serverDataSource,
            localDataSource = localDataSource,
            movieDescriptionDataMapper = movieDescriptionDataMapper,
            movieDataMapper = movieDataMapper,
            popularMoviesDataMapper = popularMoviesDataMapper,
        )
    }

    @Provides
    @Singleton
    fun provideMovieDao(localDatabase: AppDatabase): MovieDao {
        return localDatabase.movieDao
    }

    @Provides
    @Singleton
    fun provideDataSource(movieDao: MovieDao): LocalDataSource {
        return LocalDataSource(movieDao)
    }

    private class RequestInterceptor: Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val originalRequest = chain.request()
            val originalUrl = originalRequest.url
            val url = originalUrl.newBuilder()
                .addQueryParameter("api_key", "0154126bcc52cfe539c99204454466a9")
                .build()

            val requestBuilder = originalRequest.newBuilder().url(url)
            val request = requestBuilder.build()
            return chain.proceed(request)
        }
    }

}