package com.kounalem.moviedatabase.database.movie.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tvshow")
internal data class TvShowEntity(
    @PrimaryKey
    val id: Int? = null,
    val overview: String?,
    val posterPath: String?,
    val title: String?,
    val adult: Boolean,
    val originalName: String,
    val firstAirDate: String,
    val languages: List<String>?,
    val lastAirDate: String?,
    val seasons: List<SeasonEntity>?,
    val type: String?,
    val isFavourite: Boolean,
)

@Entity(tableName = "season")
data class SeasonEntity(
    @PrimaryKey
    val id: Int,
    val airDate: String,
    val episodeCount: Int,
    val name: String,
    val overview: String,
    val posterPath: String?,
    val seasonNumber: Int,
    val voteAverage: Double
)