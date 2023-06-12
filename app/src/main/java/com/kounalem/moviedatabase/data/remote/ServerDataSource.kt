package com.kounalem.moviedatabase.data.remote

import com.kounalem.moviedatabase.data.remote.models.MovieDescriptionDTO
import com.kounalem.moviedatabase.data.remote.models.PopularMoviesDTO
import javax.inject.Inject

class ServerDataSource @Inject constructor(var service: MoviesApiService) {
    suspend fun getMovieById(id: Int): MovieDescriptionDTO = service.getMovieById(id)

    suspend fun nowPlaying(pageNo: Int): PopularMoviesDTO = service.nowPlaying(pageNo)
}
