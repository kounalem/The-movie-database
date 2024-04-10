package com.kounalem.moviedatabase.core.data.movie

import com.kounalem.moviedatabase.core.data.Outcome
import com.kounalem.moviedatabase.domain.models.Movie
import com.kounalem.moviedatabase.domain.models.MovieDescription
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    val movies: Flow<Outcome<List<Movie>>>
    fun getServerMovies(pageNo: Int)
    fun search(query: String): Flow<List<Movie>>
    fun getMovieByIdObs(id: Int = -1): Flow<Outcome<MovieDescription>>
    suspend fun updateMovieFavStatus(id: Int)
}