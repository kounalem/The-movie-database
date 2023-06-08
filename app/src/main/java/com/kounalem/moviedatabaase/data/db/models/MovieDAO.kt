package com.kounalem.moviedatabaase.data.db.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movie")
data class MovieDAO(
    @PrimaryKey
    val id: Int? = null,
    val originalTitle: String?,
    val overview: String?,
    val posterPath: String?,
    val title: String?,
    val voteAverage: Double?,
)
