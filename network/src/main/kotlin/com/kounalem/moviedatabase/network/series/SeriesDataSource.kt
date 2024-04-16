package com.kounalem.moviedatabase.network.series

import com.kounalem.moviedatabase.domain.models.TvShow
import com.kounalem.moviedatabase.network.NetworkResponse
import kotlinx.coroutines.flow.Flow

interface SeriesDataSource {
    fun getSeriesById(id: Int): Flow<NetworkResponse<TvShow>>
    fun popular(pageNo: Int): Flow<NetworkResponse<List<TvShow>>>
}