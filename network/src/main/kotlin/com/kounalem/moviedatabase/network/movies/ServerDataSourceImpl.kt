package com.kounalem.moviedatabase.network.movies

import com.kounalem.moviedatabase.domain.models.MovieDescription
import com.kounalem.moviedatabase.domain.models.PopularMovies
import com.kounalem.moviedatabase.network.NetworkResponse
import com.kounalem.moviedatabase.network.movies.mappers.map
import com.kounalem.moviedatabase.network.wrapServiceCall
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class ServerDataSourceImpl @Inject constructor(
    private val service: MoviesApiService,
) : ServerDataSource {
    override fun getMovieById(id: Int): Flow<NetworkResponse<MovieDescription>> =
        wrapServiceCall(
            call = { service.getMovieById(id) },
            mapper = { it.map() }
        )

    override fun nowPlaying(pageNo: Int): Flow<NetworkResponse<PopularMovies>> =
        wrapServiceCall(
            call = { service.nowPlaying(pageNo) },
            mapper = { it.map() }
        )
}