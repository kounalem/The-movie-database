package com.kounalem.moviedatabase.data.mappers

import com.kounalem.moviedatabase.data.db.models.RoomMovie
import com.kounalem.moviedatabase.data.db.models.RoomPopularMovies
import com.kounalem.moviedatabase.data.remote.models.PopularMoviesDTO
import com.kounalem.moviedatabase.domain.models.PopularMovies
import javax.inject.Inject

class PopularMoviesDataMapper @Inject constructor() {
    fun map(input: RoomPopularMovies): PopularMovies {
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

    fun map (input: PopularMoviesDTO): RoomPopularMovies{
       return  RoomPopularMovies(
            page = input.page,
            movies = input.movies?.distinctBy { it.id }?.sortedBy { it.title }
                ?.mapTo(ArrayList()) {
                    RoomMovie(
                        originalTitle = it.originalTitle,
                        overview = it.overview,
                        posterPath = it.poster_path,
                        title = it.title,
                        voteAverage = it.voteAverage,
                        id = it.id
                    )
                },
            totalPages = input.totalPages,
            totalResults = input.totalResults,
        )
    }
}