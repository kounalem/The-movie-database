package com.kounalem.moviedatabase.presentation.popular

import com.kounalem.moviedatabase.domain.models.Movie

interface PopularMoviesContract {

    data class State(
        val isLoading: Boolean,
        val movies: List<Movie>,
        val errorText: String?,
        val page: Int,
        val endReached: Boolean,
        val searchQuery: String,
        val isRefreshing: Boolean,
    )

    sealed class MovieListingsEvent {
        data class OnSearchQueryChange(val query: String) : MovieListingsEvent()
        object Refresh: MovieListingsEvent()
    }

}