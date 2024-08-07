package com.kounalem.moviedatabase.network.di

import com.kounalem.moviedatabase.datastore.UserPreferencesDatastore
import com.kounalem.moviedatabase.network.BuildConfig
import com.kounalem.moviedatabase.network.ServerInfo
import com.kounalem.moviedatabase.network.movies.MoviesApiService
import com.kounalem.moviedatabase.network.movies.MoviesDataSource
import com.kounalem.moviedatabase.network.movies.ServerDataSourceImpl
import com.kounalem.moviedatabase.network.series.SeriesApiService
import com.kounalem.moviedatabase.network.series.SeriesDataSource
import com.kounalem.moviedatabase.network.series.SeriesDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    @MovieClient
    fun provideRetrofit(
        UserPreferencesDatastore: UserPreferencesDatastore,
        @com.kounalem.moviedatabase.shared.annotation.Application appScope: CoroutineScope,
    ): Retrofit {
        val deferredBaseUrl = appScope.async {
            UserPreferencesDatastore.userData.first().environment.name
        }

        val deferred: String = runBlocking {
            deferredBaseUrl.await()
        }

        val baseUrl = ServerInfo.getInfo(deferred)

        return Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create()).client(
                OkHttpClient.Builder().addInterceptor(
                    HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BASIC
                    },
                ).addInterceptor(
                    Interceptor { chain ->
                        val originalRequest = chain.request()
                        val originalUrl = originalRequest.url
                        val url =
                            originalUrl.newBuilder()
                                .addQueryParameter("api_key", BuildConfig.DATABASE_API_KEY).build()

                        val requestBuilder = originalRequest.newBuilder().url(url)
                        val request = requestBuilder.build()
                        chain.proceed(request)
                    },
                ).build(),
            ).build()
    }

    @Provides
    @Singleton
    internal fun provideMovieApi(
        @MovieClient retrofit: Retrofit,
    ): MoviesApiService = retrofit.create()

    @Provides
    @Singleton
    internal fun provideSeriesApi(
        @MovieClient retrofit: Retrofit,
    ): SeriesApiService = retrofit.create()

    @Provides
    @Singleton
    internal fun provideServerMoviesDataSource(service: MoviesApiService): MoviesDataSource =
        ServerDataSourceImpl(service = service)

    @Provides
    @Singleton
    internal fun provideServerSeriesDataSource(service: SeriesApiService): SeriesDataSource =
        SeriesDataSourceImpl(service = service)
}
