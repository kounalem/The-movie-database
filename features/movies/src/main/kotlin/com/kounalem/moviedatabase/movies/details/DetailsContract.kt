package com.kounalem.moviedatabase.movies.details

internal interface DetailsContract {
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

    sealed class MovieDetailsEvent {
        object FavouriteAction : MovieDetailsEvent()
    }

}