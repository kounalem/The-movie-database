package com.kounalem.moviedatabase.data.remote.mapper.mappers

import com.kounalem.moviedatabase.data.remote.models.MovieDescriptionDTO
import com.kounalem.moviedatabase.domain.models.MovieDescription
import javax.inject.Inject

class MovieDescriptionMapper @Inject constructor() {
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