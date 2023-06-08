package com.kounalem.moviedatabase.network.movies.models

import com.google.gson.annotations.SerializedName

internal data class MovieDescriptionDTO(
    val id: Int?,
    @SerializedName("original_title")
    val originalTitle: String?,
    val overview: String?,
    val popularity: Double,
    @SerializedName("poster_path")
    val posterPath: String?,
    val status: String?,
    val tagline: String?,
    val title: String?,
    @SerializedName("vote_average")
    val voteAverage: Double?,
)
