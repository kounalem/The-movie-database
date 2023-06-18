package com.kounalem.moviedatabase.data.db.mapper

import com.kounalem.moviedatabase.data.db.models.RoomMovieDescription
import com.kounalem.moviedatabase.domain.models.MovieDescription
import javax.inject.Inject

class DescriptionDataMapper @Inject constructor() {
    fun mapToDomain(input: RoomMovieDescription): MovieDescription {
        return MovieDescription(
            id = input.id ?: -1,
            originalTitle = input.originalTitle.orEmpty(),
            overview = input.overview.orEmpty(),
            posterPath = input.posterPath.orEmpty(),
            title = input.title.orEmpty(),
            voteAverage = input.voteAverage ?: 0.0,
            isFavourite = input.isFavourite,
        )
    }

    fun mapToRoom(input: MovieDescription): RoomMovieDescription {
        return RoomMovieDescription(
            id = input.id,
            originalTitle = input.originalTitle,
            overview = input.overview,
            posterPath = input.posterPath,
            title = input.title,
            voteAverage = input.voteAverage,
            isFavourite = input.isFavourite,
        )
    }
}