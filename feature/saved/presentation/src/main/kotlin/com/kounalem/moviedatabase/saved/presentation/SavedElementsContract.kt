package com.kounalem.moviedatabase.saved.presentation

internal interface SavedElementsContract {
    sealed interface State {
        data object Loading : State

        data class Elements(
            val title: String,
            val info: List<Info>,
        ) : State {
            data class Info(
                val id: Int,
                val posterPath: String,
                val title: String,
                val overview: String,
                val type: Type,
            )

            enum class Type { Movie, Show }
        }
    }
}
