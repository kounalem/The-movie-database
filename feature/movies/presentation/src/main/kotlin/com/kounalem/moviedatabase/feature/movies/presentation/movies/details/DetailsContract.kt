package com.kounalem.moviedatabase.feature.movies.presentation.movies.details

sealed interface DetailsContract {
    sealed interface State {
        data object Loading : State

        @JvmInline
        value class Error(val value: String) : State

        data class Info(
            val title: String,
            val overview: String,
            val rate: String,
            val poster: String?,
            val isFavourite: Boolean,
        ) : State
    }
}
