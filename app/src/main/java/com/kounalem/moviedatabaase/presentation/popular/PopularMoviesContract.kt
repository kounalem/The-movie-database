package com.kounalem.moviedatabaase.presentation.popular

import com.kounalem.moviedatabaase.domain.models.Movie

interface PopularMoviesContract {

    data class State(
        val isLoading: Boolean,
        val movies: List<Movie>,
        val errorText: String?,
        val page: Int,
        val endReached: Boolean,
        val searchQuery: String,
    )

    sealed class MovieListingsEvent {
        data class OnSearchQueryChange(val query: String) : MovieListingsEvent()
    }

}