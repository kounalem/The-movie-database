package com.kounalem.moviedatabase.network.series.mappers

import com.kounalem.moviedatabase.domain.models.MovieDescription
import com.kounalem.moviedatabase.domain.models.TvShow
import com.kounalem.moviedatabase.network.movies.models.MovieDescriptionDTO
import com.kounalem.moviedatabase.network.series.models.TvShowDTO

internal fun TvShowDTO.map(): TvShow {
    return TvShow(
        id = id,
        adult = adult,
        originCountry = origin_country,
        originalLanguage = original_language,
        originalName = original_name,
        overview = overview,
        popularity = popularity,
        posterPath = getPosterPath(poster_path),
        firstAirDate = first_air_date,
        name = name,
        voteAverage = vote_average,
        voteCount = vote_count,
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