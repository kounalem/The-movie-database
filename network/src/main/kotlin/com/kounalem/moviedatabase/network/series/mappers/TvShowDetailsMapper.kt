package com.kounalem.moviedatabase.network.series.mappers

import com.kounalem.moviedatabase.domain.models.TvShow
import com.kounalem.moviedatabase.network.series.models.TvShowDetailsResponseDTO

internal fun TvShowDetailsResponseDTO.map(): TvShow {
    return TvShow(
        id = id,
        adult = adult,
        originalName = original_name,
        overview = overview,
        posterPath = getPosterPath(poster_path),
        firstAirDate = first_air_date,
        name = name,
        languages = languages,
        lastAirDate = last_air_date,
        seasons = seasons.map {
            TvShow.Season(
                airDate = it.air_date,
                episodeCount = it.episode_count,
                id = it.id,
                name = it.name,
                overview = it.overview,
                posterPath = getPosterPath(it.poster_path),
                seasonNumber = it.season_number,
                voteAverage = it.vote_average
            )
        },
        type = type,
        isFavourite = false,
    )
}

private fun getPosterPath(posterPath: String?): String {
    posterPath?.let {
        return "https://image.tmdb.org/t/p/w342$posterPath"
    }
    return ""
}