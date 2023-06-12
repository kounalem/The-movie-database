package com.kounalem.moviedatabase.presentation.details

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kounalem.moviedatabase.domain.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.lifecycle.SavedStateHandle
import com.kounalem.moviedatabase.presentation.destinations.MovieDetailsDestination
import com.kounalem.moviedatabase.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val movieRepository: MovieRepository,
    savedStateHandle: SavedStateHandle,
) :
    ViewModel() {
    private val info = MovieDetailsDestination.argsFrom(savedStateHandle)
    private var isFavourite = mutableStateOf(false)
    val state = MutableStateFlow(
        DetailsContract.State(
            isLoading = true,
            title = info.title,
            poster = "",
            errorText = null,
            isFavourite = isFavourite.value,
            overview = info.overview,
            rate = "rate: ${info.rate}",
        )
    )

    init {
        viewModelScope.launch {
            state.value = when (val details = movieRepository.getMovieById(info.id)) {
                is Resource.Error -> state.value.copy(
                    errorText = "Data could not be retrieved.",
                    isLoading = false,
                    poster = "",
                    overview = "",
                )

                is Resource.Loading -> state.value.copy(
                    isLoading = true,
                    errorText = null,
                )

                is Resource.Success -> {
                    isFavourite.value = details.data?.isFavourite ?: false
                    state.value.copy(
                        errorText = null,
                        isLoading = false,
                        poster = details.data?.posterPath.orEmpty(),
                        isFavourite = isFavourite.value,
                        overview = details.data?.overview.orEmpty(),
                    )
                }
            }
        }
    }

    fun onEvent(event: DetailsContract.MovieDetailsEvent) {
        when (event) {
            DetailsContract.MovieDetailsEvent.FavouriteAction -> {
                viewModelScope.launch {
                    isFavourite.value = !isFavourite.value
                    state.value = state.value.copy(isFavourite = isFavourite.value)
                    movieRepository.favouriteAction(info.id, isFavourite.value)
                }
            }
        }
    }
}