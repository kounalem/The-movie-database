package com.kounalem.moviedatabase.network.movies

import com.kounalem.moviedatabase.network.movies.models.MovieDescriptionDTO
import com.kounalem.moviedatabase.network.movies.models.PopularMoviesDTO
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

 internal interface MoviesApiService {
    @GET("movie/now_playing")
    suspend fun nowPlaying(@Query("page") page: Int): PopularMoviesDTO

    @GET("movie/{movie_id}")
    suspend fun getMovieById(@Path("movie_id") id: Int): MovieDescriptionDTO
}