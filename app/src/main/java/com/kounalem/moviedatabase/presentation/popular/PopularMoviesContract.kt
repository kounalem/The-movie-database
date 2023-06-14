package com.kounalem.moviedatabase.presentation.popular


interface PopularMoviesContract {
    sealed interface State {
        data class Loading(val currentInfo: List<Info.Movie>) : State
        @JvmInline
        value class Error(val message: String) : State
        data class Info(
            val movies: List<Movie>,
            val isRefreshing: Boolean,
            val searchQuery: String?,
            val endReached:Boolean,
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