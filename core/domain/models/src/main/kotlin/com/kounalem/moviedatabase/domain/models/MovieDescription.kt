package com.kounalem.moviedatabase.domain.models

data class MovieDescription(
    val id: Int,
    val originalTitle: String,
    val overview: String,
    val posterPath: String,
    val title: String,
    val voteAverage: Double,
    val isFavourite: Boolean
) {
    val rate = "Movie rating: $voteAverage"
}