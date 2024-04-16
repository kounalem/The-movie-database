package com.kounalem.moviedatabase.network.movies

import com.kounalem.moviedatabase.network.movies.models.PopularMoviesDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

 internal interface MoviesApiService {
    @GET("movie/now_playing")
    suspend fun nowPlaying(@Query("page") page: Int): Response<PopularMoviesDTO>
}