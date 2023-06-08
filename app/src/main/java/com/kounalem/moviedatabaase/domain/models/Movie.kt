package com.kounalem.moviedatabaase.domain.models

data class Movie(
    val id: Int,
    val posterPath: String,
    val title: String,
    val voteAverage: Double,
    val overview: String,
)