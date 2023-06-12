package com.kounalem.moviedatabaase.data.mappers

import com.kounalem.moviedatabaase.data.db.models.RoomMovieDescription
import com.kounalem.moviedatabaase.data.remote.models.MovieDescriptionDTO
import com.kounalem.moviedatabaase.domain.models.MovieDescription
import javax.inject.Inject

class MovieDescriptionDataMapper @Inject constructor() {
    fun map(input: RoomMovieDescription): MovieDescription {
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

    fun map(input: MovieDescriptionDTO): MovieDescription {
        return MovieDescription(
            id = input.id ?: -1,
            originalTitle = input.original_title.orEmpty(),
            overview = input.overview.orEmpty(),
            posterPath = getPosterPath(input.poster_path),
            title = input.title.orEmpty(),
            voteAverage = input.vote_average ?: 0.0,
            isFavourite = false,
        )
    }

    private fun getPosterPath(posterPath: String?): String {
        posterPath?.let {
            return "https://image.tmdb.org/t/p/w342$posterPath"
        }
        return ""
    }
}