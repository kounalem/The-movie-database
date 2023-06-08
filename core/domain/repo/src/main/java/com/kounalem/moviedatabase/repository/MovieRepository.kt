package com.kounalem.moviedatabase.repository

import com.kounalem.moviedatabase.domain.models.Movie
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    val movies: Flow<Outcome<List<Movie>>>

    fun getMoviesForPage(pageNo: Int)

    fun search(query: String): Flow<List<Movie>>

    fun getMovieById(id: Int): Flow<Outcome<Movie>>

    suspend fun updateMovieFavStatus(id: Int)

    fun getAllLocalSavedMovies(): Flow<List<Movie>>
    suspend fun clearLocalInfo()
}
