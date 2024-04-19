package com.kounalem.moviedatabase.show.presentation.details

sealed interface DetailsContract {

    sealed interface State {
        data object Loading : State

        @JvmInline
        value class Error(val value: String) : State
        data class Info(
            val title: String,
            val overview: String,
            val poster: String?,
            val isFavourite: Boolean,
            val seasons: List<Season>?,
            val languages: List<String>?,
            val firstAirDate: String?,
            val lastAirDate: String,
            val type: String?,
        ) : State {
            data class Season(
                val airDate: String,
                val episodeCount: Int,
                val id: Int,
                val name: String,
                val overview: String,
                val posterPath: String?,
                val seasonNumber: Int,
                val voteAverage: Int
            )
        }
    }

}