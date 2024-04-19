package com.kounalem.moviedatabase.network.series.mappers

import com.kounalem.moviedatabase.domain.models.TvShow
import com.kounalem.moviedatabase.network.series.models.TvShowDTO

internal fun TvShowDTO.map(): TvShow {
    return TvShow(
        id = id,
        adult = adult,
        originalName = original_name,
        overview = overview,
        posterPath = getPosterPath(poster_path),
        firstAirDate = first_air_date,
        name = name,
        languages = null,
        lastAirDate = null,
        seasons = null,
        type = null,
        isFavourite = false,
    )
}

private fun getPosterPath(posterPath: String?): String {
    posterPath?.let {
        return "https://image.tmdb.org/t/p/w342$posterPath"
    }
    return ""
}