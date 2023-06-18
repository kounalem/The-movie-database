package com.kounalem.moviedatabase.presentation.popular

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kounalem.moviedatabase.domain.MovieRepository
import com.kounalem.moviedatabase.domain.models.PopularMovies
import com.kounalem.moviedatabase.util.IO
import com.kounalem.moviedatabase.util.paginator.PaginatorFactory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.kounalem.moviedatabase.util.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce

@HiltViewModel
class PopularMoviesViewModel @Inject constructor(
    private val movieRepository: MovieRepository,
    @IO private val ioDispatcher: CoroutineDispatcher,
    paginatorFactory: PaginatorFactory<Int, Resource<PopularMovies>>
) :
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

    private var paginator = paginatorFactory.create(
        initialKey = state.value.page,
        onRequest = { nextPage ->
            movieRepository.nowPlaying(nextPage)
        },
        getNextKey = {
            state.value.page + 1
        },
        onUpdate = { item, newkey ->
            if (item.message != null) {
                state.value = state.value.copy(
                    isLoading = false,
                    errorText = "Data could not be retrieved.",
                    endReached = false,
                    isRefreshing = false,
                )
            } else {
                val fetchedMovies = item.data?.movies ?: emptyList()
                state.value = state.value.copy(
                    isLoading = false,
                    errorText = null,
                    movies = (state.value.movies + fetchedMovies),
                    page = newkey,
                    endReached = item.data?.totalPages == state.value.page,
                    isRefreshing = false,
                )
            }
        },
        onLoadUpdated = {
            state.value = state.value.copy(isLoading = true)
        }
    )

    init {
        loadNextItems()
    }

    fun loadNextItems() {
        viewModelScope.launch(ioDispatcher) {
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
        paginator.reset()
        loadNextItems()
    }

    fun onEvent(event: PopularMoviesContract.MovieListingsEvent) {
        when (event) {
            is PopularMoviesContract.MovieListingsEvent.OnSearchQueryChange -> {
                state.value = state.value.copy(searchQuery = event.query)
                searchJob?.cancel()
                searchJob = viewModelScope.launch(ioDispatcher) {
                    movieRepository.search(event.query).debounce(500L).collectLatest {
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