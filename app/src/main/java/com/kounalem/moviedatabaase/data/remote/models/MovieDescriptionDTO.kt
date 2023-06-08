package com.kounalem.moviedatabaase.data.remote.models

import com.google.gson.annotations.SerializedName

class MovieDescriptionDTO(

    @SerializedName("id")
    val id: Int?,

    @SerializedName("original_title")
    val original_title: String?,

    @SerializedName("overview")
    val overview: String?,

    @SerializedName("popularity")
    val popularity: Double,

    @SerializedName("poster_path")
    val poster_path: String?,

    @SerializedName("status")
    val status: String?,

    @SerializedName("tagline")
    val tagline: String?,

    @SerializedName("title")
    val title: String?,

    @SerializedName("vote_average")
    val vote_average: Double?,
)