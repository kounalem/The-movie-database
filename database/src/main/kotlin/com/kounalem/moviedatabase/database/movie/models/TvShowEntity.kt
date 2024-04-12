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
    val voteAverage: Double?,
    val adult: Boolean,
    val originCountry: List<String>,
    val originalLanguage: String,
    val originalName: String,
    val popularity: Double,
    val firstAirDate: String,
    val voteCount: Int,
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
    val voteAverage: Int
)