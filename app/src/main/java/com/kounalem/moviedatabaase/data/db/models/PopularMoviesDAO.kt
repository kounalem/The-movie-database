package com.kounalem.moviedatabaase.data.db.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "popular_movies")
class PopularMoviesDAO(
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val page: Int?,
    val movies: ArrayList<MovieDAO>?,
    val totalPages: Int?,
    val totalResults: Int?
)