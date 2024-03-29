package com.kounalem.moviedatabase.network.movies

import com.kounalem.moviedatabase.domain.models.MovieDescription
import com.kounalem.moviedatabase.domain.models.PopularMovies
import com.kounalem.moviedatabase.network.movies.mappers.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

internal class ServerDataSourceImpl @Inject constructor(
    private val service: MoviesApiService,
) : ServerDataSource {
    override fun getMovieById(id: Int): Flow<MovieDescription> =
        flow {
            val movie = service.getMovieById(id)
            emit(movie.map())
        }

    override suspend fun nowPlaying(pageNo: Int): PopularMovies {
        val playing = service.nowPlaying(pageNo)
        return playing.map()
    }
}