package com.kounalem.moviedatabase.core.data.series

import com.kounalem.moviedatabase.repository.Outcome
import com.kounalem.moviedatabase.core.data.mapToOutcome
import com.kounalem.moviedatabase.database.movie.LocalDataSource
import com.kounalem.moviedatabase.domain.models.TvShow
import com.kounalem.moviedatabase.network.NetworkResponse
import com.kounalem.moviedatabase.network.series.SeriesDataSource
import com.kounalem.moviedatabase.repository.TvShowRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

internal class TvShowRepositoryImpl @Inject constructor(
    private val server: SeriesDataSource,
    private val local: LocalDataSource,
    private val coroutineScope: CoroutineScope,
) : TvShowRepository {

    private val errors: MutableStateFlow<String?> = MutableStateFlow(null)
    private val _tvShows =
        local.getAllShows().onStart { getServerTvShows(pageNo = 1) }
    override val tvShows: Flow<Outcome<List<TvShow>>>
        get() = combine(_tvShows, errors) { result, e ->
            if (result.isEmpty() && !e.isNullOrEmpty()) {
                Outcome.Error(e)
            } else {
                Outcome.Success(result)
            }
        }

    override fun search(query: String): Flow<List<TvShow>> =
        local.getFilteredShows(query).distinctUntilChanged()

    override fun getServerTvShows(pageNo: Int) {
        coroutineScope.launch {
            server.popular(pageNo).collect { result ->
                if (result is NetworkResponse.Success) {
                    local.saveShowList(result.body)
                    errors.value = null
                } else {
                    errors.value = "Could not retrieve shows"
                }
            }
        }
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getTvShowByIdObs(id: Int): Flow<Outcome<TvShow>> =
        local.getShowById(id).flatMapLatest { description ->
            if (description != null && description.seasons?.isNotEmpty() == true) {
                flowOf(Outcome.Success(description))
            } else {
                server.getSeriesById(id).mapToOutcome { description ->
                    coroutineScope.launch {
                        local.saveShowDescription(description)
                    }
                    description
                }
            }
        }.catch {
            Outcome.Exception<TvShow>(it)
        }

    override suspend fun updateTvShowFavStatus(id: Int) = local.updateShowFavStatus(id)
}