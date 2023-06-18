package com.kounalem.moviedatabase.data.remote.mapper.mappers

import com.kounalem.moviedatabase.data.remote.models.PopularMoviesDTO
import com.kounalem.moviedatabase.domain.models.PopularMovies
import javax.inject.Inject

class PopularMoviesMapper @Inject constructor(private val movieMapper: MovieMapper) {
    fun map(input: PopularMoviesDTO): PopularMovies {
        val movieList = input.movies?.let {
            it.map { item ->
                movieMapper.map(item)
            }.sortedBy { it.date }
        } ?: emptyList()

        return PopularMovies(
            page = input.page ?: 0,
            movies = movieList,
            totalPages = input.totalPages ?: 0,
            totalResults = input.totalResults ?: 0
        )
    }

}