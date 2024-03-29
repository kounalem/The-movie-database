package com.kounalem.moviedatabase.domain.models

data class PopularMovies(
    val page: Int,
    val movies: List<Movie>,
    val totalPages: Int,
    val totalResults: Int
)