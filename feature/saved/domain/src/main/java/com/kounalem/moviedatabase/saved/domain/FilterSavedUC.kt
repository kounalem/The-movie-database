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
                    SavedElement.Movie(movie.id, movie.posterPath)
                }
            )
            savedElements.addAll(
                shows.filter { it.isFavourite }.map { movie ->
                    SavedElement.TvShow(movie.id, movie.posterPath)
                }
            )
            savedElements
        }

    sealed interface SavedElement {
        val id: Int
        val image: String

        data class Movie(override val id: Int, override val image: String) : SavedElement
        data class TvShow(override val id: Int, override val image: String) : SavedElement
    }

}