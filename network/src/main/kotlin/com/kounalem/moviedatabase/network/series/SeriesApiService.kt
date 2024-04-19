package com.kounalem.moviedatabase.network.series

import com.kounalem.moviedatabase.network.series.models.TvShowDetailsResponseDTO
import com.kounalem.moviedatabase.network.series.models.TvShowResponseDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

internal interface SeriesApiService {
    @GET("tv/popular")
    suspend fun popular(@Query("page") page: Int): Response<TvShowResponseDTO>

    @GET("tv/{series_id}")
    suspend fun getSeriesById(@Path("series_id") id: Int): Response<TvShowDetailsResponseDTO>
}