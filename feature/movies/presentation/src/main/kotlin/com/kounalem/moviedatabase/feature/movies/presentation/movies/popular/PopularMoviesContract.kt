package com.kounalem.moviedatabase.feature.movies.presentation.movies.popular

internal interface PopularMoviesContract {
    sealed interface Event {
        data class NavigateToDetails(val id: Int) : Event
    }

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
            val savedMoviesFilter: SavedMoviesFilter,
        ) : State {
            data class SavedMoviesFilter(
                val filterText: String,
                val isFiltering: Boolean,
            )

            data class Movie(
                val id: Int,
                val posterPath: String,
                val title: String,
                val overview: String,
                val isFavourite: Boolean = false,
            )
        }
    }
}
