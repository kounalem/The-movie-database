package com.kounalem.moviedatabase.feature.movies.presentation.movies.details

import com.kounalem.moviedatanase.core.ui.navigation.NavRoute

interface Navigation {
    data object Details : NavRoute {
        const val DETAILS_ID = "id"
        override val path: NavRoute.Path
            get() = NavRoute.Path("details")
    }
}

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

    sealed class MovieDetailsEvent {
        object FavouriteAction : MovieDetailsEvent()
    }

}