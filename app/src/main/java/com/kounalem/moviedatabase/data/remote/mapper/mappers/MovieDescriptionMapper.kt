package com.kounalem.moviedatabase.data.remote.mapper.mappers

import com.kounalem.moviedatabase.data.remote.models.MovieDescriptionDTO
import com.kounalem.moviedatabase.domain.models.MovieDescription

internal fun MovieDescriptionDTO.map(): MovieDescription {
    return MovieDescription(
        id = this.id ?: -1,
        originalTitle = this.original_title.orEmpty(),
        overview = this.overview.orEmpty(),
        posterPath = getPosterPath(this.poster_path),
        title = this.title.orEmpty(),
        voteAverage = this.vote_average ?: 0.0,
        isFavourite = false,
    )
}

private fun getPosterPath(posterPath: String?): String {
    posterPath?.let {
        return "https://image.tmdb.org/t/p/w342$posterPath"
    }
    return ""
}