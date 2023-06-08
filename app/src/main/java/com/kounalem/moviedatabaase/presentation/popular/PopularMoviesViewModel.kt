package com.kounalem.moviedatabaase.presentation.popular

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kounalem.moviedatabaase.domain.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.kounalem.moviedatabaase.util.Resource
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest

@HiltViewModel
class PopularMoviesViewModel @Inject constructor(private val movieRepository: MovieRepository) :
    ViewModel() {

    private var searchJob: Job? = null

     val state = MutableStateFlow(
        PopularMoviesContract.State(
            isLoading = false,
            movies = emptyList(),
            errorText = null,
            page = 1,
            endReached = false,
            searchQuery = ""
        )
    )

    private val paginator = Paginator(
        initialKey = state.value.page,
        onLoadUpdated = {
            state.value = state.value.copy(isLoading = it)
        },
        onRequest = { nextPage ->
            movieRepository.nowPlaying(nextPage)
        },
        getNextKey = {
            state.value.page + 1
        },
        onError = {
            state.value = state.value.copy(
                isLoading = false,
                errorText = it?.localizedMessage,
                endReached = false,
            )
        },
        onSuccess = { items, newKey ->
            state.value = state.value.copy(
                isLoading = false,
                errorText = null,
                movies = state.value.movies + items.movies,
                page = newKey,
                endReached = items.movies.isEmpty()
            )
        }
    )

    init {
        loadNextItems()
    }

    fun loadNextItems() {
        viewModelScope.launch {
            paginator.loadNextItems()
        }
    }

    fun onEvent(event: PopularMoviesContract.MovieListingsEvent) {
        when (event) {
            is PopularMoviesContract.MovieListingsEvent.OnSearchQueryChange -> {
                state.value = state.value.copy(searchQuery = event.query)
                searchJob?.cancel()
                searchJob = viewModelScope.launch {
                    delay(500L)
                    movieRepository.search(event.query).collectLatest {
                        state.value = when (it) {
                            is Resource.Error -> state.value.copy(
                                isLoading = false,
                                errorText = "Data could not be retrieved.",
                                endReached = false,
                            )

                            is Resource.Loading -> {
                                state.value.copy(
                                    isLoading = it.isLoading
                                )
                            }

                            is Resource.Success -> {
                                state.value.copy(
                                    movies = it.data.orEmpty(),
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}