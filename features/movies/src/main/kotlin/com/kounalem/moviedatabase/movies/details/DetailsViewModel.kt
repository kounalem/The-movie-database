package com.kounalem.moviedatabase.movies.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kounalem.moviedatabase.core.data.Resource
import com.kounalem.moviedatabase.core.data.movie.MovieRepository
import com.kounalem.moviedatabase.domain.models.MovieDescription
import com.kounalem.moviedatabase.navigation.NavRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class DetailsViewModel @Inject constructor(
    private val movieRepository: MovieRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val error: MutableStateFlow<String?> = MutableStateFlow(null)
    private val isLoading = MutableStateFlow(false)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val result: StateFlow<DetailsContract.State.Info?> =
        savedStateHandle.getStateFlow<Int?>(
            key = NavRoute.Details.DETAILS_ID,
            null,
        ).onEach {
            isLoading.value = true
        }.flatMapLatest { id ->
            if (id == null) {
                error.value = "Not correct ID path"
                emptyFlow()
            } else {
                movieRepository.getMovieByIdObs(id).flatMapLatest {
                    when (it) {
                        is Resource.Error -> {
                            error.value = it.message
                            emptyFlow()
                        }

                        is Resource.Success<MovieDescription> -> {
                            it.data?.let { description ->
                                flowOf(
                                    DetailsContract.State.Info(
                                        title = description.title,
                                        overview = description.overview,
                                        rate = description.rate,
                                        poster = description.posterPath,
                                        isFavourite = description.isFavourite,
                                    )
                                )
                            } ?: run {
                                emptyFlow()
                            }

                        }
                    }
                }
            }
        }
            .onEach {
                isLoading.value = false
                error.value = null
            }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    val state: StateFlow<DetailsContract.State>
        get() = combine(isLoading, error, result) { isLoading, error, result ->
            if (error != null)
                DetailsContract.State.Error(error)
            else if (isLoading)
                DetailsContract.State.Loading
            else result ?: DetailsContract.State.Loading
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            DetailsContract.State.Loading
        )

    fun onEvent(event: DetailsContract.MovieDetailsEvent) {
        when (event) {
            DetailsContract.MovieDetailsEvent.FavouriteAction -> {
                viewModelScope.launch {
                    savedStateHandle.get<Int?>(NavRoute.Details.DETAILS_ID)?.let { id ->
                        movieRepository.updateMovieFavStatus(id)
                    }
                }
            }
        }
    }
}