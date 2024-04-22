package com.kounalem.moviedatabase.saved.domain

import com.kounalem.moviedatabase.repository.MovieRepository
import com.kounalem.moviedatabase.repository.TvShowRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class FilterSavedUC @Inject constructor(
    private val moveRepo: MovieRepository,
    private val showRepo: TvShowRepository,
) {
    fun invoke(): Flow<List<SavedElement>> =

        combine(
            moveRepo.getAllLocalSavedMovies(),
            showRepo.getAllLocalSavedShows()
        ) { movies, shows ->
            val savedElements = mutableListOf<SavedElement>()
            savedElements.addAll(
                movies.filter { it.isFavourite }.map { movie ->
                    SavedElement.Movie(movie.id, movie.posterPath, movie.title, movie.overview)
                }
            )
            savedElements.addAll(
                shows.filter { it.isFavourite }.map { show ->
                    SavedElement.TvShow(show.id, show.posterPath, show.name, show.overview)
                }
            )
            savedElements
        }

    sealed interface SavedElement {
        val id: Int
        val image: String
        val title: String
        val overview: String
        data class Movie(
            override val id: Int,
            override val image: String,
            override val title: String,
            override val overview: String
        ) : SavedElement

        data class TvShow(
            override val id: Int,
            override val image: String,
            override val title: String,
            override val overview: String
        ) : SavedElement
    }

}