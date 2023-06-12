package com.kounalem.moviedatabase.data.db.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movie_description")
data class RoomMovieDescription(
    @PrimaryKey
    val id: Int? = null,
    val originalTitle: String?,
    val overview: String?,
    val popularity: Double,
    val posterPath: String?,
    val status: String?,
    val tagline: String?,
    val title: String?,
    val voteAverage: Double?,
    val isFavourite: Boolean,
)