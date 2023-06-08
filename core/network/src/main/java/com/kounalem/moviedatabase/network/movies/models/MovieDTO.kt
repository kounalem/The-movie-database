package com.kounalem.moviedatabase.network.movies.models

import com.google.gson.annotations.SerializedName

internal data class MovieDTO(
    val id: Int?,
    @SerializedName("original_title")
    val originalTitle: String?,
    val overview: String?,
    @SerializedName("poster_path")
    val posterPath: String?,
    val title: String?,
    @SerializedName("vote_average")
    val voteAverage: Double?,
    @SerializedName("release_date")
    val date: String,
)
