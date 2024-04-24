package com.kounalem.moviedatabase.feature.movies.presentation.movies.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.kounalem.moviedatabase.domain.models.Movie
import com.kounalem.moviedatabase.feature.movies.presentation.movies.details.navigation.Navigation
import com.kounalem.moviedatabase.repository.MovieRepository
import com.kounalem.moviedatabase.repository.Outcome
import com.kounalem.moviedatanase.core.ui.BaseViewModelImpl
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
internal class DetailsViewModel
    @Inject
    constructor(
        private val movieRepository: MovieRepository,
        private val savedStateHandle: SavedStateHandle,
    ) : BaseViewModelImpl<DetailsContract.State, Unit>() {
        private val error: MutableStateFlow<String?> = MutableStateFlow(null)
        private val isLoading = MutableStateFlow(false)

        @OptIn(ExperimentalCoroutinesApi::class)
        private val result: StateFlow<DetailsContract.State.Info?> =
            savedStateHandle.getStateFlow<Int?>(
                key = Navigation.Details.DETAILS_ID,
                null,
            ).onEach {
                isLoading.value = true
            }.flatMapLatest { id ->
                if (id == null) {
                    error.value = "Not correct ID path"
                    emptyFlow()
                } else {
                    movieRepository.getMovieById(id).flatMapLatest {
                        when (it) {

                            is Outcome.Exception,
                            is Outcome.Error,
                            -> {
                                error.value = it.message
                                emptyFlow()
                            }

                            is Outcome.Success<Movie> -> {
                                it.data?.let { description ->
                                    flowOf(
                                        DetailsContract.State.Info(
                                            title = description.title,
                                            overview = description.overview,
                                            rate = description.rate,
                                            poster = description.posterPath,
                                            isFavourite = description.isFavourite,
                                        ),
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

        override val uiState: StateFlow<DetailsContract.State>
            get() =
                combine(isLoading, error, result) { isLoading, error, result ->
                    if (error != null) {
                        DetailsContract.State.Error(error)
                    } else if (isLoading) {
                        DetailsContract.State.Loading
                    } else {
                        result ?: DetailsContract.State.Loading
                    }
                }.stateIn(
                    viewModelScope,
                    SharingStarted.WhileSubscribed(5_000),
                    DetailsContract.State.Loading,
                )

        fun onFavouriteClicked() {
            viewModelScope.launch {
                savedStateHandle.get<Int?>(Navigation.Details.DETAILS_ID)?.let { id ->
                    movieRepository.updateMovieFavStatus(id)
                }
            }
        }
    }
