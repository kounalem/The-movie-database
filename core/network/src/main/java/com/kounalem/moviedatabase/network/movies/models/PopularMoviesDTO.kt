package com.kounalem.moviedatabase.network.movies.models

import com.google.gson.annotations.SerializedName

internal data class PopularMoviesDTO(
    val page: Int?,
    @SerializedName("results")
    val movies: List<MovieDTO>?,
    @SerializedName("total_pages")
    val totalPages: Int?,
    @SerializedName("total_results")
    val totalResults: Int?,
)
