package com.kounalem.moviedatabase.network.series.models

data class TvShowResponseDTO(
    val page: Int,
    val results: List<TvShowDTO>,
    val total_pages: Int,
    val total_results: Int,
)
