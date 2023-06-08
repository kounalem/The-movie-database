package com.kounalem.moviedatabaase.data.mappers

import com.kounalem.moviedatabaase.data.db.models.MovieDescriptionDAO
import com.kounalem.moviedatabaase.domain.models.MovieDescription

internal class MovieDescriptionDataMapper {
    fun map(input: MovieDescriptionDAO): MovieDescription {
        return MovieDescription(
            id = input.id ?: -1,
            originalTitle = input.originalTitle.orEmpty(),
            overview = input.overview.orEmpty(),
            posterPath = getPosterPath(input.posterPath),
            title = input.title.orEmpty(),
            voteAverage = input.voteAverage ?: 0.0,
            isFavourite = input.isFavourite,
        )
    }

    private fun getBackdropPath(backdropPath: String?): String {
        backdropPath?.let {
            return "https://image.tmdb.org/t/p/w780$backdropPath"
        }
        return ""
    }

    private fun getPosterPath(posterPath: String?): String {
        posterPath?.let {
            return "https://image.tmdb.org/t/p/w342$posterPath"
        }
        return ""
    }
}