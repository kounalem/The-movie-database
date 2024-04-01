package com.kounalem.moviedatabase.feature.movies.domain.usecase

import com.kounalem.moviedatabase.core.data.movie.MovieRepository
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.debounce
import javax.inject.Inject
import com.kounalem.moviedatabase.domain.models.Movie

class FilterMoviesUC @Inject constructor(
    private val repo: MovieRepository,
) {
    @OptIn(FlowPreview::class)
    fun invoke(query: String): Flow<List<Movie>> =
        repo.search(query).debounce(DEBOUNCE)

    private companion object {
        const val DEBOUNCE = 125L
    }
}