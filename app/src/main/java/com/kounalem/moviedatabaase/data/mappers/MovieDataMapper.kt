package com.kounalem.moviedatabaase.data.mappers

import com.kounalem.moviedatabaase.data.db.models.MovieDAO
import com.kounalem.moviedatabaase.domain.models.Movie

internal class MovieDataMapper {
    fun map(input: MovieDAO): Movie {
        return Movie(
            id = input.id ?: -1,
            posterPath = getPosterPath(input.posterPath),
            title = input.title.orEmpty(),
            voteAverage = input.voteAverage ?: 0.0,
            overview = input.overview.orEmpty(),
        )
    }

    private fun getPosterPath(posterPath: String?): String {
        posterPath?.let {
            return "https://image.tmdb.org/t/p/w342$posterPath"
        }
        return ""
    }

}