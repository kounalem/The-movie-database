package com.kounalem.moviedatabase.domain.models

data class Movie(
    val id: Int,
    val posterPath: String,
    val title: String,
    val voteAverage: Double,
    val overview: String,
    val date: Long,
    val page: Int,
    val isFavourite: Boolean,
    val originalTitle: String,
) {
    val rate = "Movie rating: $voteAverage"
}
