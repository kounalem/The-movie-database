package com.kounalem.moviedatabase.network.series.models

data class TvShowDetailsResponseDTO(
    val adult: Boolean,
    val episode_run_time: List<Int>?,
    val first_air_date: String,
    val genres: List<GenreDTO>,
    val homepage: String?,
    val id: Int,
    val in_production: Boolean,
    val languages: List<String>,
    val last_air_date: String,
    val name: String,
    val original_name: String,
    val overview: String,
    val poster_path: String?,
    val seasons: List<SeasonDTO>,
    val status: String,
    val tagline: String?,
    val type: String,
) {

    data class GenreDTO(
        val id: Int,
        val name: String
    )

    data class SeasonDTO(
        val air_date: String,
        val episode_count: Int,
        val id: Int,
        val name: String,
        val overview: String,
        val poster_path: String?,
        val season_number: Int,
        val vote_average: Double
    )

}