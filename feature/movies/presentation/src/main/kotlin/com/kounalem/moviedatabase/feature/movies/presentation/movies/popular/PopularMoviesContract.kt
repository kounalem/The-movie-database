package com.kounalem.moviedatabase.feature.movies.presentation.movies.popular

import com.kounalem.moviedatanase.core.ui.navigation.NavRoute

interface Navigation {
    data object Home : NavRoute {
        override val path: NavRoute.Path
            get() = NavRoute.Path("home")
    }

}

internal interface PopularMoviesContract {

    sealed interface State {
        data object Loading : State

        @JvmInline
        value class Error(val message: String) : State
        data class Info(
            val movies: List<Movie>,
            val isRefreshing: Boolean,
            val searchQuery: String?,
            val endReached: Boolean,
            val fetchingNewMovies: Boolean,
        ) : State {
            data class Movie(
                val id: Int,
                val posterPath: String,
                val title: String,
                val overview: String,
            )
        }
    }


    sealed class MovieListingsEvent {
        data class OnSearchQueryChange(val query: String) : MovieListingsEvent()
        object Refresh : MovieListingsEvent()
    }

}