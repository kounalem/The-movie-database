package com.kounalem.moviedatabase.network.movies.mappers

import com.kounalem.moviedatabase.domain.models.Movie
import com.kounalem.moviedatabase.network.movies.models.MovieDTO
import java.time.LocalDate
import java.time.ZoneOffset

internal fun MovieDTO.map(pageNo: Int): Movie {
    return Movie(
        id = this.id ?: -1,
        posterPath = getPosterPath(this.posterPath),
        title = this.title.orEmpty(),
        voteAverage = this.voteAverage ?: 0.0,
        overview = this.overview.orEmpty(),
        date = getTimeStamp(this.date),
        isFavourite = false,
        originalTitle = this.originalTitle.orEmpty(),
        page = pageNo
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
    return startOfDay.toEpochMilli()
}

