package com.kounalem.moviedatabase.data.remote.mapper.mappers

import com.kounalem.moviedatabase.data.remote.models.MovieDTO
import com.kounalem.moviedatabase.domain.models.Movie
import java.time.LocalDate
import java.time.ZoneOffset
import javax.inject.Inject

class MovieMapper @Inject constructor() {
    fun map(input: MovieDTO): Movie {
        return Movie(
            id = input.id ?: -1,
            posterPath = getPosterPath(input.poster_path),
            title = input.title.orEmpty(),
            voteAverage = input.voteAverage ?: 0.0,
            overview = input.overview.orEmpty(),
            date = getTimeStamp(input.date)
        )
    }

    private fun getPosterPath(posterPath: String?): String {
        posterPath?.let {
            return "https://image.tmdb.org/t/p/w342$posterPath"
        }
        return ""
    }

    private fun getTimeStamp(date: String): Long {
        val releaseDate = LocalDate.parse(date)
        val startOfDay = releaseDate.atStartOfDay().toInstant(ZoneOffset.UTC)
        return  startOfDay.toEpochMilli()
    }

}