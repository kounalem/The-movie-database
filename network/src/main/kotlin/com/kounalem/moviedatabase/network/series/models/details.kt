package com.kounalem.moviedatabase.network.series.models

data class TvShowDetailsResponseDTO(
    val adult: Boolean,
    val backdrop_path: String?,
    val created_by: List<String>?,
    val episode_run_time: List<Int>?,
    val first_air_date: String,
    val genres: List<GenreDTO>,
    val homepage: String?,
    val id: Int,
    val in_production: Boolean,
    val languages: List<String>,
    val last_air_date: String,
    val last_episode_to_air: EpisodeDTO,
    val name: String,
    val next_episode_to_air: EpisodeDTO,
    val networks: List<NetworkDTO>,
    val number_of_episodes: Int,
    val number_of_seasons: Int,
    val origin_country: List<String>,
    val original_language: String,
    val original_name: String,
    val overview: String,
    val popularity: Double,
    val poster_path: String?,
    val production_companies: List<ProductionCompanyDTO>,
    val production_countries: List<ProductionCountryDTO>,
    val seasons: List<SeasonDTO>,
    val spoken_languages: List<SpokenLanguageDTO>,
    val status: String,
    val tagline: String?,
    val type: String,
    val vote_average: Double,
    val vote_count: Int
) {

    data class GenreDTO(
        val id: Int,
        val name: String
    )

    data class EpisodeDTO(
        val id: Int,
        val name: String,
        val overview: String,
        val vote_average: Int,
        val vote_count: Int,
        val air_date: String,
        val episode_number: Int,
        val episode_type: String,
        val production_code: String,
        val runtime: Int,
        val season_number: Int,
        val show_id: Int,
        val still_path: String?
    )

    data class NetworkDTO(
        val id: Int,
        val logo_path: String?,
        val name: String,
        val origin_country: String
    )

    data class ProductionCompanyDTO(
        val id: Int,
        val logo_path: String?,
        val name: String,
        val origin_country: String
    )

    data class ProductionCountryDTO(
        val iso_3166_1: String,
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
        val vote_average: Int
    )

    data class SpokenLanguageDTO(
        val english_name: String,
        val iso_639_1: String,
        val name: String
    )
}