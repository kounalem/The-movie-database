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
            searchQuery = "",
            isRefreshing = false,
        )
    )

    private val paginator = Paginator(
        initialKey = state.value.page,
        onRequest = { nextPage ->
            movieRepository.nowPlaying(nextPage)
        },
        getNextKey = {
            state.value.page + 1
        },
    )

    init {
        viewModelScope.launch {
            paginator.result().collectLatest {
                when (it) {
                    is Resource.Error -> {
                        state.value = state.value.copy(
                            isLoading = false,
                            errorText = it.message,
                            endReached = false,
                            isRefreshing = false,
                        )
                    }

                    is Resource.Success -> {
                        val fetchedMovies = it.data?.first?.movies ?: emptyList()
                        state.value = state.value.copy(
                            isLoading = false,
                            errorText = null,
                            movies = (state.value.movies + fetchedMovies).distinctBy { it.id },
                            page = it.data?.second ?: 1,
                            endReached = it.data?.first?.totalPages == state.value.page,
                            isRefreshing = false,
                        )
                    }

                    is Resource.Loading -> state.value = state.value.copy(isLoading = true)
                }
            }
        }

        loadNextItems()
    }

    fun loadNextItems() {
        viewModelScope.launch {
            paginator.loadNextItems()
        }
    }

    private fun refreshElements() {
        state.value = PopularMoviesContract.State(
            isLoading = false,
            movies = emptyList(),
            errorText = null,
            page = 1,
            endReached = false,
            searchQuery = "",
            isRefreshing = true,
        )
        loadNextItems()
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
                                    isLoading = true
                                )
                            }

                            is Resource.Success -> {
                                state.value.copy(
                                    isLoading = false,
                                    movies = it.data.orEmpty(),
                                )
                            }
                        }
                    }
                }
            }

            PopularMoviesContract.MovieListingsEvent.Refresh -> refreshElements()
        }
    }
}