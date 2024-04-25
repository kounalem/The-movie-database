package com.kounalem.moviedatabase.feature.movies.presentation.movies.popular

import androidx.lifecycle.viewModelScope
import com.kounalem.moviedatabase.feature.movies.domain.usecase.FilterMoviesUC
import com.kounalem.moviedatabase.feature.movies.domain.usecase.GetMostPopularMoviesUC
import com.kounalem.moviedatabase.repository.MovieRepository
import com.kounalem.moviedatabase.core.ui.BaseViewModelImpl
import com.kounalem.moviedatabase.core.ui.emitAsync
import com.kounalem.moviedatabase.core.ui.paginator.Paginator
import com.zhuinden.flowcombinetuplekt.combineTuple
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class PopularMoviesViewModel
    @Inject
    constructor(
        private val repo: MovieRepository,
        private val filterMoviesUC: FilterMoviesUC,
        private val popularMoviesUC: GetMostPopularMoviesUC,
    ) : BaseViewModelImpl<PopularMoviesContract.State, PopularMoviesContract.Event>() {
        private val isLoading = MutableStateFlow(false)
        private val error: MutableStateFlow<String?> = MutableStateFlow(null)

        private var endReached: Boolean = false
        private val isRefreshing = MutableStateFlow(false)
        private val filterSavedMovies =
            MutableStateFlow(
                PopularMoviesContract.State.Info.SavedMoviesFilter(
                    filterText = "All movies",
                    isFiltering = false,
                ),
            )
        private val searchQuery: MutableStateFlow<String?> = MutableStateFlow(null)
        private val fetchingNewMovies: MutableStateFlow<Boolean> = MutableStateFlow(false)
        private val filteredMovies: MutableStateFlow<List<PopularMoviesContract.State.Info.Movie>> =
            MutableStateFlow(emptyList())
        private val paginator: Paginator<Int> =
            Paginator(initialKey = 1, onRequest = { nextPage ->
                fetchingNewMovies.value = true
                repo.getMoviesForPage(nextPage)
            }, getNextKey = { currentKey ->
                currentKey + 1
            })

        @OptIn(ExperimentalCoroutinesApi::class)
        private val results: StateFlow<List<PopularMoviesContract.State.Info.Movie>> =
            popularMoviesUC.movies.onEach {
                isRefreshing.value = true
                isLoading.value = true
            }.flatMapLatest { movie ->
                flowOf(
                    movie.map {
                        PopularMoviesContract.State.Info.Movie(
                            id = it.value.id,
                            title = it.value.title,
                            posterPath = it.value.posterPath,
                            overview = it.value.overview,
                            isFavourite = it.value.isFavourite,
                        )
                    },
                )
            }.catch {
                error.value = it.message
            }.onEach {
                isLoading.value = false
                error.value = null
                isRefreshing.value = false
                fetchingNewMovies.value = false
            }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

        override val uiState: StateFlow<PopularMoviesContract.State>
            get() =
                combineTuple(
                    results,
                    isLoading,
                    error,
                    searchQuery,
                    isRefreshing,
                    filteredMovies,
                    fetchingNewMovies,
                    filterSavedMovies,
                )
                    .map { (movies, isLoading, error, searchQuery, isRefreshing, filteredMovies, fetchingNewMovies, filterSavedMovies) ->
                        if (error != null) {
                            PopularMoviesContract.State.Error(error)
                        } else if (isLoading) {
                            if (movies.toList()
                                    .isEmpty()
                            ) {
                                PopularMoviesContract.State.Loading
                            } else {
                                PopularMoviesContract.State.Info(
                                    movies =
                                        if (filterSavedMovies.isFiltering) {
                                            movies.toList()
                                                .filter { it.isFavourite }
                                        } else {
                                            movies.toList()
                                        },
                                    isRefreshing = isRefreshing,
                                    searchQuery = searchQuery,
                                    endReached = endReached,
                                    fetchingNewMovies = true,
                                    savedMoviesFilter = filterSavedMovies,
                                )
                            }
                        } else if (filteredMovies.isNotEmpty() && searchQuery?.isNotEmpty() == true) {
                            PopularMoviesContract.State.Info(
                                movies = filteredMovies,
                                isRefreshing = false,
                                searchQuery = searchQuery,
                                endReached = false,
                                fetchingNewMovies = false,
                                savedMoviesFilter = filterSavedMovies,
                            )
                        } else {
                            PopularMoviesContract.State.Info(
                                movies =
                                    if (filterSavedMovies.isFiltering) {
                                        movies.toList()
                                            .filter { it.isFavourite }
                                    } else {
                                        movies.toList()
                                    },
                                isRefreshing = isRefreshing,
                                searchQuery = searchQuery,
                                endReached = endReached,
                                fetchingNewMovies = fetchingNewMovies,
                                savedMoviesFilter = filterSavedMovies,
                            )
                        }
                    }.stateIn(
                        viewModelScope,
                        SharingStarted.WhileSubscribed(5_000),
                        PopularMoviesContract.State.Loading,
                    )

        fun loadNextItems() {
            viewModelScope.launch {
                paginator.loadNextItems()
            }
        }

        fun refreshElements() {
            isRefreshing.value = true
            paginator.reset()
            loadNextItems()
        }

        fun onSearchQueryChange(query: String) {
            searchQuery.value = query
            viewModelScope.launch {
                filterMoviesUC.invoke(query).collect { movieList ->
                    val query =
                        movieList.map { movie ->
                            PopularMoviesContract.State.Info.Movie(
                                id = movie.id,
                                title = movie.title,
                                posterPath = movie.posterPath,
                                overview = movie.overview,
                                isFavourite = movie.isFavourite,
                            )
                        }
                    filteredMovies.value = query
                }
            }
        }

        fun onSavedMoviesClicked() {
            filterSavedMovies.value =
                if (filterSavedMovies.value.isFiltering) {
                    PopularMoviesContract.State.Info.SavedMoviesFilter(
                        filterText = "All movies",
                        isFiltering = false,
                    )
                } else {
                    PopularMoviesContract.State.Info.SavedMoviesFilter(
                        filterText = "Just saved movies",
                        isFiltering = true,
                    )
                }
        }

        fun navigateToDetails(id: Int) {
            events.emitAsync(PopularMoviesContract.Event.NavigateToDetails(id))
        }

        fun updateElementInfo(
            id: Int?,
            state: Boolean?,
        ) {
            if (id == null || state == null) return
            popularMoviesUC.updateElement(
                id = id,
                status = state,
            )
        }
    }
