package com.kounalem.moviedatabase.show.presentation.popular


internal interface PopularShowsContract {

    sealed interface State {
        data object Loading : State

        @JvmInline
        value class Error(val message: String) : State
        data class Info(
            val shows: List<Show>,
            val isRefreshing: Boolean,
            val searchQuery: String?,
            val endReached: Boolean,
            val fetchingNewShows: Boolean,
        ) : State {
            data class Show(
                val id: Int,
                val posterPath: String,
                val title: String,
                val overview: String,
            )
        }
    }


    sealed interface Event {
        data class NavigateToDetails(val id: Int): Event
    }

}