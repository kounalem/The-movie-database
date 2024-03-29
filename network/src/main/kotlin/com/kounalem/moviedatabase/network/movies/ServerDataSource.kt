package com.kounalem.moviedatabase.network.movies

import com.kounalem.moviedatabase.domain.models.MovieDescription
import com.kounalem.moviedatabase.domain.models.PopularMovies
import kotlinx.coroutines.flow.Flow

interface ServerDataSource {
    fun getMovieById(id: Int): Flow<MovieDescription>
    suspend fun nowPlaying(pageNo: Int): PopularMovies
}