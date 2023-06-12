package com.kounalem.moviedatabase.data.remote.models

import com.google.gson.annotations.SerializedName
class PopularMoviesDTO(
    @SerializedName("page")
    val page: Int?,

    @SerializedName("results")
    val movies: List<MovieDTO>?,

    @SerializedName("total_pages")
    val totalPages: Int?,

    @SerializedName("total_results")
    val totalResults: Int?
)
