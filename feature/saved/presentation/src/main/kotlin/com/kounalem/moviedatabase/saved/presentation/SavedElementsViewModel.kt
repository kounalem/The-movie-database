package com.kounalem.moviedatabase.saved.presentation

import androidx.lifecycle.viewModelScope
import com.kounalem.moviedatabase.saved.domain.FilterSavedUC
import com.kounalem.moviedatanase.core.ui.BaseViewModelImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
internal class SavedElementsViewModel
    @Inject
    constructor(
        private val filterSavedInfoUseCase: FilterSavedUC,
    ) : BaseViewModelImpl<SavedElementsContract.State, Unit>() {
        private val isLoading = MutableStateFlow(false)

        @OptIn(ExperimentalCoroutinesApi::class)
        private val results: StateFlow<List<SavedElementsContract.State.Elements.Info>> =
            filterSavedInfoUseCase.invoke().onEach {
                isLoading.value = true
            }
                .mapLatest { list ->
                    list.map { item ->
                        SavedElementsContract.State.Elements.Info(
                            id = item.id,
                            posterPath = item.image,
                            title = item.title,
                            overview = item.overview,
                            type = if (item is FilterSavedUC.SavedElement.Movie) SavedElementsContract.State.Elements.Type.Movie else SavedElementsContract.State.Elements.Type.Show,
                        )
                    }
                }
                .onEach {
                    isLoading.value = false
                }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

        override val uiState: StateFlow<SavedElementsContract.State>
            get() =
                combine(
                    results,
                    isLoading,
                ) { res, isLoading ->
                    if (isLoading) {
                        SavedElementsContract.State.Loading
                    } else {
                        SavedElementsContract.State.Elements(title = "Saved Elements", info = res)
                    }
                }
                    .stateIn(
                        viewModelScope,
                        SharingStarted.WhileSubscribed(5_000),
                        SavedElementsContract.State.Loading,
                    )
    }
