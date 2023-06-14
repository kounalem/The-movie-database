package com.kounalem.moviedatabase.data.db.mapper

import com.kounalem.moviedatabase.data.db.models.MovieDescriptionEntity
import com.kounalem.moviedatabase.domain.models.MovieDescription

internal fun MovieDescriptionEntity.mapToDomain(): MovieDescription {
    return MovieDescription(
        id = this.id ?: -1,
        originalTitle = this.originalTitle.orEmpty(),
        overview = this.overview.orEmpty(),
        posterPath = this.posterPath.orEmpty(),
        title = this.title.orEmpty(),
        voteAverage = this.voteAverage ?: 0.0,
        isFavourite = this.isFavourite,
    )
}

internal fun MovieDescription.mapToEntity(): MovieDescriptionEntity {
    return MovieDescriptionEntity(
        id = this.id,
        originalTitle = this.originalTitle,
        overview = this.overview,
        posterPath = this.posterPath,
        title = this.title,
        voteAverage = this.voteAverage,
        isFavourite = this.isFavourite,
    )
}
