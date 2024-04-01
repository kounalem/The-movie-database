package com.kounalem.moviedatabase.feature.movies.presentation.movies.popular

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kounalem.moviedatabase.core.data.Outcome
import com.kounalem.moviedatabase.core.data.movie.MovieRepository
import com.kounalem.moviedatabase.feature.movies.domain.usecase.FilterMoviesUC
import com.kounalem.moviedatanase.core.ui.paginator.Paginator
import com.kounalem.moviedatanase.core.ui.paginator.PaginatorFactory
import com.zhuinden.flowcombinetuplekt.combineTuple
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class PopularMoviesViewModel @Inject constructor(
    private val repo: MovieRepository,
    paginatorFactory: PaginatorFactory<Int>,
    private val filterMoviesUC: FilterMoviesUC,
) : ViewModel() {

    private val isLoading = MutableStateFlow(false)
    private val error: MutableStateFlow<String?> = MutableStateFlow(null)
    private val currentList: MutableStateFlow<LinkedHashSet<PopularMoviesContract.State.Info.Movie>> =
        MutableStateFlow(LinkedHashSet())

    private var endReached: Boolean = false
    private val isRefreshing = MutableStateFlow(false)
    private val searchQuery: MutableStateFlow<String?> = MutableStateFlow(null)
    private val fetchingNewMovies: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private val filteredMovies: MutableStateFlow<List<PopularMoviesContract.State.Info.Movie>> =
        MutableStateFlow(emptyList())
    private val paginator: Paginator<Int> by lazy {
        paginatorFactory.create(
            initialKey = 2,
            onRequest = { nextPage ->
                fetchingNewMovies.value = true
                repo.getServerMovies(nextPage)
            },
            getNextKey = { currentKey ->
                currentKey + 1
            }
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private val results: StateFlow<LinkedHashSet<PopularMoviesContract.State.Info.Movie>> =
        repo.movies.onEach {
            isRefreshing.value = true
            isLoading.value = true
        }
            .flatMapLatest {
                when (it) {
                    is Outcome.Error -> {
                        error.value = it.message
                        emptyFlow()
                    }

                    is Outcome.Success -> {
                        this.endReached = it.data?.isEmpty() ?: true
                        it.data?.let { data ->
                            currentList.value += data.map { movie ->
                                PopularMoviesContract.State.Info.Movie(
                                    id = movie.id,
                                    title = movie.title,
                                    posterPath = movie.posterPath,
                                    overview = movie.overview,
                                )
                            }
                            currentList
                        } ?: run {
                            error.value = "No info available"
                            emptyFlow()
                        }
                    }
                }
            }
            .onEach {
                isLoading.value = false
                error.value = null
                isRefreshing.value = false
                fetchingNewMovies.value = false
            }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), LinkedHashSet())

    val state: StateFlow<PopularMoviesContract.State>
        get() = combineTuple(
            results,
            isLoading,
            error,
            searchQuery,
            isRefreshing,
            filteredMovies,
            fetchingNewMovies,
        )
            .map { (movies, isLoading, error, searchQuery, isRefreshing, filteredMovies, fetchingNewMovies) ->
                if (error != null)
                    PopularMoviesContract.State.Error(error)
                else if (isLoading)
                    PopularMoviesContract.State.Loading(movies.toList())
                else if (movies.isEmpty())
                    PopularMoviesContract.State.Loading(movies.toList())
                else if (filteredMovies.isNotEmpty() && searchQuery?.isNotEmpty() == true) {
                    PopularMoviesContract.State.Info(
                        movies = filteredMovies,
                        isRefreshing = false,
                        searchQuery = searchQuery,
                        endReached = false,
                        fetchingNewMovies = false
                    )
                } else
                    PopularMoviesContract.State.Info(
                        movies = movies.toList(),
                        isRefreshing = isRefreshing,
                        searchQuery = searchQuery,
                        endReached = endReached,
                        fetchingNewMovies = fetchingNewMovies
                    )
            }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                PopularMoviesContract.State.Loading(emptyList())
            )

    fun loadNextItems() {
        viewModelScope.launch {
            paginator.loadNextItems()
        }
    }

    private fun refreshElements() {
        isRefreshing.value = true
        paginator.reset()
        loadNextItems()
    }

    fun onEvent(event: PopularMoviesContract.MovieListingsEvent) {
        when (event) {
            is PopularMoviesContract.MovieListingsEvent.OnSearchQueryChange -> {
                searchQuery.value = event.query
                viewModelScope.launch {
                    filterMoviesUC.invoke(event.query).collect { movieList ->
                        val query = movieList.map { movie ->
                            PopularMoviesContract.State.Info.Movie(
                                id = movie.id,
                                title = movie.title,
                                posterPath = movie.posterPath,
                                overview = movie.overview,
                            )
                        }
                        filteredMovies.value = query
                    }
                }
            }

            PopularMoviesContract.MovieListingsEvent.Refresh -> refreshElements()
        }
    }
}

// check the tests
// do the workaround for the details