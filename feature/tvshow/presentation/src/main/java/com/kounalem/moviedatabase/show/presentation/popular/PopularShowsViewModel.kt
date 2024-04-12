package com.kounalem.moviedatabase.show.presentation.popular

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kounalem.moviedatabase.repository.Outcome
import com.kounalem.moviedatabase.repository.TvShowRepository
import com.kounalem.moviedatabase.tvshow.domain.FilterShowsUC
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
internal class PopularShowsViewModel @Inject constructor(
    private val repo: TvShowRepository,
    paginatorFactory: PaginatorFactory<Int>,
    private val filterShowUc: FilterShowsUC,
) : ViewModel() {

    private val isLoading = MutableStateFlow(false)
    private val error: MutableStateFlow<String?> = MutableStateFlow(null)
    private val currentList: MutableStateFlow<LinkedHashSet<PopularShowsContract.State.Info.Show>> =
        MutableStateFlow(LinkedHashSet())

    private var endReached: Boolean = false
    private val isRefreshing = MutableStateFlow(false)
    private val searchQuery: MutableStateFlow<String?> = MutableStateFlow(null)
    private val fetchingNewMovies: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private val filteredMovies: MutableStateFlow<List<PopularShowsContract.State.Info.Show>> =
        MutableStateFlow(emptyList())
    private val paginator: Paginator<Int> by lazy {
        paginatorFactory.create(
            initialKey = 2,
            onRequest = { nextPage ->
                fetchingNewMovies.value = true
                repo.getServerTvShows(nextPage)
            },
            getNextKey = { currentKey ->
                currentKey + 1
            }
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private val results: StateFlow<LinkedHashSet<PopularShowsContract.State.Info.Show>> =
        repo.tvShows.onEach {
            isRefreshing.value = true
            isLoading.value = true
        }
            .flatMapLatest {
                when (it) {
                    is Outcome.Exception,
                    is Outcome.Error -> {
                        error.value = it.message
                        emptyFlow()
                    }

                    is Outcome.Success -> {
                        this.endReached = it.data?.isEmpty() ?: true
                        it.data?.let { data ->
                            currentList.value += data.map { show ->
                                PopularShowsContract.State.Info.Show(
                                    id = show.id,
                                    title = show.name,
                                    posterPath = show.posterPath,
                                    overview = show.overview,
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

    val state: StateFlow<PopularShowsContract.State>
        get() = combineTuple(
            results,
            isLoading,
            error,
            searchQuery,
            isRefreshing,
            filteredMovies,
            fetchingNewMovies,
        )
            .map { (shows, isLoading, error, searchQuery, isRefreshing, filteredMovies, fetchingNewMovies) ->
                if (error != null)
                    PopularShowsContract.State.Error(error)
                else if (isLoading)
                    if (shows.toList().isEmpty())
                        PopularShowsContract.State.Loading
                    else
                        PopularShowsContract.State.Info(
                            shows = shows.toList(),
                            isRefreshing = isRefreshing,
                            searchQuery = searchQuery,
                            endReached = endReached,
                            fetchingNewShows = true
                        )
                else if (filteredMovies.isNotEmpty() && searchQuery?.isNotEmpty() == true) {
                    PopularShowsContract.State.Info(
                        shows = filteredMovies,
                        isRefreshing = false,
                        searchQuery = searchQuery,
                        endReached = false,
                        fetchingNewShows = false
                    )
                } else
                    PopularShowsContract.State.Info(
                        shows = shows.toList(),
                        isRefreshing = isRefreshing,
                        searchQuery = searchQuery,
                        endReached = endReached,
                        fetchingNewShows = fetchingNewMovies
                    )
            }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                PopularShowsContract.State.Loading
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

    fun onEvent(event: PopularShowsContract.Event) {
        when (event) {
            is PopularShowsContract.Event.OnSearchQueryChange -> {
                searchQuery.value = event.query
                viewModelScope.launch {
                    filterShowUc.invoke(event.query).collect { movieList ->
                        val query = movieList.map { movie ->
                            PopularShowsContract.State.Info.Show(
                                id = movie.id,
                                title = movie.name,
                                posterPath = movie.posterPath,
                                overview = movie.overview,
                            )
                        }
                        filteredMovies.value = query
                    }
                }
            }

            PopularShowsContract.Event.Refresh -> refreshElements()
        }
    }
}