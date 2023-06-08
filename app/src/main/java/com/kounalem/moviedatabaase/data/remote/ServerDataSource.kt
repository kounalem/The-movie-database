package com.kounalem.moviedatabaase.data.remote

import com.kounalem.moviedatabaase.data.remote.models.MovieDescriptionDTO
import com.kounalem.moviedatabaase.data.remote.models.PopularMoviesDTO
import javax.inject.Inject

class ServerDataSource @Inject constructor(var service: MoviesApiService) {
    suspend fun getMovieById(id: Int): MovieDescriptionDTO = service.getMovieById(id)

    suspend fun nowPlaying(pageNo: Int): PopularMoviesDTO = service.nowPlaying(pageNo)
}
