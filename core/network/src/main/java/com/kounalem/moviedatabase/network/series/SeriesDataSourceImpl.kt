package com.kounalem.moviedatabase.network.series

import com.kounalem.moviedatabase.domain.models.TvShow
import com.kounalem.moviedatabase.network.NetworkResponse
import com.kounalem.moviedatabase.network.series.mappers.map
import com.kounalem.moviedatabase.network.wrapServiceCall
import kotlinx.coroutines.flow.Flow

internal class SeriesDataSourceImpl(private val service: SeriesApiService) : SeriesDataSource {
    override fun getSeriesById(id: Int): Flow<NetworkResponse<TvShow>> =
        wrapServiceCall(
            call = { service.getSeriesById(id) },
            mapper = { dto -> dto.map() },
        )

    override fun popular(pageNo: Int): Flow<NetworkResponse<List<TvShow>>> =
        wrapServiceCall(
            call = { service.popular(pageNo) },
            mapper = { dto ->
                dto.results.map { showDto ->
                    showDto.map()
                }
            },
        )
}
