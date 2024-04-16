package com.kounalem.moviedatabase.database.movie.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movie")
internal data class MovieEntity(
    @PrimaryKey
    val id: Int? = null,
    val overview: String?,
    val posterPath: String?,
    val title: String?,
    val voteAverage: Double?,
    val date: Long,
    val page: Int,
    val originalTitle: String?,
    val isFavourite: Boolean,
)
