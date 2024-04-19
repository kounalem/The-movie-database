package com.kounalem.moviedatabase.database.movie.mapper

import com.kounalem.moviedatabase.database.movie.models.SeasonEntity
import com.kounalem.moviedatabase.database.movie.models.TvShowEntity
import com.kounalem.moviedatabase.domain.models.TvShow

internal fun TvShowEntity.mapToDomain(): TvShow {
    return TvShow(
        adult = adult,
        id = id ?: -1,
        isFavourite = isFavourite,
        originalName = originalName,
        overview = overview.orEmpty(),
        posterPath = posterPath.orEmpty(),
        firstAirDate = firstAirDate,
        name = title.orEmpty(),
        languages = languages,
        lastAirDate = lastAirDate,
        type = type,
        seasons = seasons?.map {
            it.mapToDomain()
        }
    )
}

fun SeasonEntity.mapToDomain() = TvShow.Season(
    id = id,
    airDate = airDate,
    episodeCount = episodeCount,
    name = name,
    overview = overview,
    posterPath = posterPath,
    seasonNumber = seasonNumber,
    voteAverage = voteAverage
)

private fun TvShow.Season.mapToEntity() =
    SeasonEntity(
        id = id,
        airDate = airDate,
        episodeCount = episodeCount,
        name = name,
        overview = overview,
        posterPath = posterPath,
        seasonNumber = seasonNumber,
        voteAverage = voteAverage
    )

internal fun TvShow.mapToEntity(): TvShowEntity {
    return TvShowEntity(
        adult = adult,
        id = id,
        isFavourite = isFavourite,
        originalName = originalName,
        overview = overview,
        posterPath = posterPath,
        firstAirDate = firstAirDate,
        title = name,
        languages = languages,
        lastAirDate = lastAirDate,
        type = type,
        seasons = seasons?.map {
            it.mapToEntity()
        },
    )
}
