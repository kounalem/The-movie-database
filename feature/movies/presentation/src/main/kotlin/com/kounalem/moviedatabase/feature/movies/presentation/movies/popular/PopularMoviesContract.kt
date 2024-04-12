package com.kounalem.moviedatabase.feature.movies.presentation.movies.popular


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


    sealed class Event {
        data class OnSearchQueryChange(val query: String) : Event()
        object Refresh : Event()
    }

}