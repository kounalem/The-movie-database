package com.kounalem.moviedatabase.data.remote

import com.kounalem.moviedatabase.data.remote.mapper.mappers.map
import com.kounalem.moviedatabase.domain.models.MovieDescription
import com.kounalem.moviedatabase.domain.models.PopularMovies
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ServerDataSource @Inject constructor(
    private val service: MoviesApiService,
) {
    fun getMovieById(id: Int): Flow<MovieDescription> =
        flow {
            val movie = service.getMovieById(id)
            emit(movie.map())
        }

    suspend fun nowPlaying(pageNo: Int): PopularMovies {
        val playing = service.nowPlaying(pageNo)
        return playing.map()
    }
}