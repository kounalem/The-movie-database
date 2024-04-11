package com.kounalem.moviedatabase.network.di

import com.kounalem.moviedatabase.network.BuildConfig
import com.kounalem.moviedatabase.network.movies.MoviesApiService
import com.kounalem.moviedatabase.network.movies.ServerDataSource
import com.kounalem.moviedatabase.network.movies.ServerDataSourceImpl
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
object NetworkModule {
    @Provides
    @Singleton
    internal fun provideMovieApi(): MoviesApiService {
        return Retrofit.Builder().baseUrl("http://api.themoviedb.org/3/")
            .addConverterFactory(GsonConverterFactory.create()).client(
                OkHttpClient.Builder().addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BASIC
                }).addInterceptor(RequestInterceptor()).build()
            ).build().create()
    }

    @Provides
    @Singleton
    internal fun provideServerDataSource(
        service: MoviesApiService,
    ): ServerDataSource = ServerDataSourceImpl(service = service)

    private class RequestInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val originalRequest = chain.request()
            val originalUrl = originalRequest.url
            val url = originalUrl.newBuilder()
                .addQueryParameter("api_key", BuildConfig.DATABASE_API_KEY).build()

            val requestBuilder = originalRequest.newBuilder().url(url)
            val request = requestBuilder.build()
            return chain.proceed(request)
        }
    }

}