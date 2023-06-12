package com.kounalem.moviedatabase.data.remote

import com.kounalem.moviedatabase.data.remote.models.MovieDescriptionDTO
import com.kounalem.moviedatabase.data.remote.models.PopularMoviesDTO
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MoviesApiService {
    @GET("movie/now_playing")
    suspend fun nowPlaying(@Query("page") page: Int): PopularMoviesDTO

    @GET("movie/{movie_id}")
    suspend fun getMovieById(@Path("movie_id") id: Int): MovieDescriptionDTO

}