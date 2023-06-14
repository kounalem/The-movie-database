package com.kounalem.moviedatabase.data.db.mapper

import com.kounalem.moviedatabase.data.db.models.MovieEntity
import com.kounalem.moviedatabase.domain.models.Movie

internal fun MovieEntity.mapToDomain(): Movie {
    return Movie(
        id = this.id ?: -1,
        posterPath = this.posterPath.orEmpty(),
        title = this.title.orEmpty(),
        voteAverage = this.voteAverage ?: 0.0,
        overview = this.overview.orEmpty(),
        date = this.date,
    )
}

internal fun Movie.mapToEntity(): MovieEntity {
    return MovieEntity(
        id = this.id,
        posterPath = this.posterPath,
        title = this.title,
        voteAverage = this.voteAverage,
        overview = this.overview,
        date = this.date,
    )
}
