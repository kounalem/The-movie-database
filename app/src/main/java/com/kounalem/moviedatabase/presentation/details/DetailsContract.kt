package com.kounalem.moviedatabase.presentation.details

interface DetailsContract {

    data class State(
        val isLoading: Boolean,
        val title: String,
        val overview: String,
        val rate: String,
        val poster: String?,
        val errorText: String?,
        val isFavourite: Boolean,
    )

    sealed class MovieDetailsEvent {
        object FavouriteAction : MovieDetailsEvent()
    }

}