package com.kounalem.moviedatabase.domain.models

data class TvShow(
    val adult: Boolean,
    val id: Int,
    val originalName: String,
    val overview: String,
    val posterPath: String,
    val firstAirDate: String,
    val name: String,
    //details
    val languages: List<String>?,
    val lastAirDate: String?,
    val seasons: List<Season>?,
    val type: String?,
    val isFavourite: Boolean,
) {
    data class Season(
        val airDate: String,
        val episodeCount: Int,
        val id: Int,
        val name: String,
        val overview: String,
        val posterPath: String?,
        val seasonNumber: Int,
        val voteAverage: Double
    )

}