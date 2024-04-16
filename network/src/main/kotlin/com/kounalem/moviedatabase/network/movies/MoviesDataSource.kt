package com.kounalem.moviedatabase.network.movies

import com.kounalem.moviedatabase.domain.models.PopularMovies
import com.kounalem.moviedatabase.network.NetworkResponse
import kotlinx.coroutines.flow.Flow

interface MoviesDataSource {
    fun nowPlaying(pageNo: Int): Flow<NetworkResponse<PopularMovies>>
}