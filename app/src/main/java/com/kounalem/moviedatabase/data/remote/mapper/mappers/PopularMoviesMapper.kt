package com.kounalem.moviedatabase.data.remote.mapper.mappers

import com.kounalem.moviedatabase.data.remote.models.PopularMoviesDTO
import com.kounalem.moviedatabase.domain.models.PopularMovies

internal fun PopularMoviesDTO.map(): PopularMovies {
    val movieList = this.movies?.let {
        it.map { item ->
            item.map()
        }.sortedBy { it.date }
    } ?: emptyList()

    return PopularMovies(
        page = this.page ?: 0,
        movies = movieList,
        totalPages = this.totalPages ?: 0,
        totalResults = this.totalResults ?: 0
    )
}