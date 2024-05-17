package com.kounalem.moviedatabase.network.movies

import com.kounalem.moviedatabase.network.movies.mappers.map
import com.kounalem.moviedatabase.network.wrapServiceCall
import javax.inject.Inject

internal class ServerDataSourceImpl
    @Inject
    constructor(
        private val service: MoviesApiService,
    ) : MoviesDataSource {
        override fun nowPlaying(pageNo: Int) =
            wrapServiceCall(
                call = { service.nowPlaying(pageNo) },
                mapper = { it.map(pageNo) },
            )
    }
