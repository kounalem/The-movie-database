package com.kounalem.moviedatabase.tvshow.domain

import com.kounalem.moviedatabase.domain.models.TvShow
import com.kounalem.moviedatabase.repository.TvShowRepository
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.debounce
import javax.inject.Inject

class FilterShowsUC
    @Inject
    constructor(
        private val repo: TvShowRepository,
    ) {
        @OptIn(FlowPreview::class)
        fun invoke(query: String): Flow<List<TvShow>> = repo.search(query).debounce(DEBOUNCE)

        private companion object {
            const val DEBOUNCE = 125L
        }
    }
