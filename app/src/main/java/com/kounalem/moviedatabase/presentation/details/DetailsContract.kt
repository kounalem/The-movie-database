package com.kounalem.moviedatabase.presentation.details

interface DetailsContract {

    sealed interface State {
        object Loading : State
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