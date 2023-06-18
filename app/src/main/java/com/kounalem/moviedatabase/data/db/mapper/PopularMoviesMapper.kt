package com.kounalem.moviedatabase.data.db.mapper

import com.kounalem.moviedatabase.data.db.models.RoomMovie
import com.kounalem.moviedatabase.data.db.models.RoomPopularMovies
import com.kounalem.moviedatabase.domain.models.PopularMovies
import javax.inject.Inject

class PopularMoviesMapper @Inject constructor() {
    fun mapToDomain(input: RoomPopularMovies): PopularMovies {
        val movieDataMapper = MovieMapper()
        val movieList = input.movies?.let {
            it.map { item ->
                movieDataMapper.mapToDomain(item)
            }.sortedBy { it.date }
        } ?: emptyList()

        return PopularMovies(
            page = input.page ?: 0,
            movies = movieList,
            totalPages = input.totalPages ?: 0,
            totalResults = input.totalResults ?: 0
        )
    }

    fun mapToRoom(input: PopularMovies): RoomPopularMovies {
        return RoomPopularMovies(
            page = input.page,
            movies = input.movies.distinctBy { it.id }.sortedBy { it.title }
                .mapTo(ArrayList()) {
                    RoomMovie(
                        overview = it.overview,
                        posterPath = it.posterPath,
                        title = it.title,
                        voteAverage = it.voteAverage,
                        id = it.id,
                        date = it.date,
                    )
                },
            totalPages = input.totalPages,
            totalResults = input.totalResults,
        )
    }
}