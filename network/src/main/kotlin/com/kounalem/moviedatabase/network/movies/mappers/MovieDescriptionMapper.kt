package com.kounalem.moviedatabase.network.movies.mappers

import com.kounalem.moviedatabase.domain.models.MovieDescription
import com.kounalem.moviedatabase.network.movies.models.MovieDescriptionDTO


internal fun MovieDescriptionDTO.map(): MovieDescription {
    return MovieDescription(
        id = this.id ?: -1,
        originalTitle = this.originalTitle.orEmpty(),
        overview = this.overview.orEmpty(),
        posterPath = getPosterPath(this.posterPath),
        title = this.title.orEmpty(),
        voteAverage = this.voteAverage ?: 0.0,
        isFavourite = false,
    )
}

private fun getPosterPath(posterPath: String?): String {
    posterPath?.let {
        return "https://image.tmdb.org/t/p/w342$posterPath"
    }
    return ""
}