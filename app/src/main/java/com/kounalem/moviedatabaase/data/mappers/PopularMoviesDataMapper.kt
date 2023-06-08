package com.kounalem.moviedatabaase.data.mappers

import com.kounalem.moviedatabaase.data.db.models.PopularMoviesDAO
import com.kounalem.moviedatabaase.domain.models.PopularMovies

internal class PopularMoviesDataMapper {
    fun map(input: PopularMoviesDAO): PopularMovies {
        val movieDataMapper = MovieDataMapper()
        val movieList = input.movies?.let {
            it.map { item ->
                movieDataMapper.map(item)
            }.sortedByDescending { it.voteAverage }
        } ?: emptyList()

        return PopularMovies(
            id = input.id?:-1,
            page = input.page ?: 0,
            movies = movieList,
            totalPages = input.totalPages ?: 0,
            totalResults = input.totalResults ?: 0
        )
    }
}