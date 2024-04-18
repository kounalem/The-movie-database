package com.kounalem.moviedatabase.feature.movies.domain.usecase

import javax.inject.Inject
import com.kounalem.moviedatabase.domain.models.Movie
import com.kounalem.moviedatabase.repository.MovieRepository
import com.kounalem.moviedatabase.repository.Outcome
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest

class GetMostPopularMoviesUC @Inject constructor(repo: MovieRepository) {
    private val refresh: MutableSharedFlow<Unit> = MutableSharedFlow(replay = 1)
    private val currentList: MutableStateFlow<LinkedHashMap<Int, Movie>> =
        MutableStateFlow(LinkedHashMap())

    @OptIn(ExperimentalCoroutinesApi::class)
    val movies = refresh.flatMapLatest {
        repo.movies.flatMapLatest {
            when (it) {
                is Outcome.Exception,
                is Outcome.Error -> {
                    throw (Throwable(it.message))
                }

                is Outcome.Success -> {
                    it.data?.let { data ->
                        currentList.value.putAll(data.associateBy { it.id })
                        currentList
                    } ?: run {
                        throw (Throwable("No movies available"))
                    }
                }
            }
        }
    }

    init {
        refresh.tryEmit(Unit)
    }

    fun updateElement(id: Int?, status: Boolean?) {
        if(id == null || status == null) return
        val currentMap = currentList.value
        currentMap[id]?.let { movie ->
            val updatedMovie = movie.copy(isFavourite = status)
            currentMap[id] = updatedMovie
        }
        currentList.value = currentMap
        refresh.tryEmit(Unit)
    }
}