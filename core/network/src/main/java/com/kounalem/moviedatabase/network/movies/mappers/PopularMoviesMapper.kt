package com.kounalem.moviedatabase.network.movies.mappers

import com.kounalem.moviedatabase.domain.models.PopularMovies
import com.kounalem.moviedatabase.network.movies.models.PopularMoviesDTO

internal fun PopularMoviesDTO.map(pageNo: Int): PopularMovies {
    val movieList =
        this.movies?.let {
            it.map { item ->
                item.map(pageNo)
            }.sortedBy { it.date }
        } ?: emptyList()

    return PopularMovies(
        page = this.page ?: 0,
        movies = movieList,
        totalPages = this.totalPages ?: 0,
        totalResults = this.totalResults ?: 0,
    )
}
