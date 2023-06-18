package com.kounalem.moviedatabase.data.db.mapper

import com.kounalem.moviedatabase.data.db.models.RoomMovie
import com.kounalem.moviedatabase.domain.models.Movie
import javax.inject.Inject

class MovieMapper @Inject constructor() {
    fun mapToDomain(input: RoomMovie): Movie {
        return Movie(
            id = input.id ?: -1,
            posterPath = input.posterPath.orEmpty(),
            title = input.title.orEmpty(),
            voteAverage = input.voteAverage ?: 0.0,
            overview = input.overview.orEmpty(),
            date = input.date,
        )
    }

    fun mapToRoom(input: Movie): RoomMovie {
        return RoomMovie(
            id = input.id,
            posterPath = input.posterPath,
            title = input.title,
            voteAverage = input.voteAverage,
            overview = input.overview,
            date = input.date,
        )
    }

}