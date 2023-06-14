package com.kounalem.moviedatabase.data.db.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movie_description")
data class MovieDescriptionEntity(
    @PrimaryKey
    val id: Int? = null,
    val originalTitle: String?,
    val overview: String?,
    val posterPath: String?,
    val title: String?,
    val voteAverage: Double?,
    val isFavourite: Boolean,
)