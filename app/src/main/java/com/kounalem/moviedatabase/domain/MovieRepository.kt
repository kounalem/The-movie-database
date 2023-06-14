package com.kounalem.moviedatabase.domain

import com.kounalem.moviedatabase.domain.models.Movie
import com.kounalem.moviedatabase.domain.models.MovieDescription
import com.kounalem.moviedatabase.domain.models.PopularMovies
import com.kounalem.moviedatabase.util.Resource
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    val movies: Flow<Resource<List<Movie>>>
    suspend fun getServerMovies(pageNo: Int)
    fun search(query: String): Flow<List<Movie>>
    fun getMovieByIdObs(id: Int = -1): Flow<Resource<MovieDescription>>
    suspend fun updateMovieFavStatus(id: Int)
}