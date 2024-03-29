package com.kounalem.moviedatabase.database.movie

import com.kounalem.moviedatabase.domain.models.Movie
import com.kounalem.moviedatabase.domain.models.MovieDescription
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
    fun getFilteredMovies(query: String): Flow<List<Movie>>

    fun getAllMovies(): Flow<List<Movie>>

    suspend fun saveMovieList(movies: List<Movie>): List<Unit>

    fun getMovieDescriptionById(movieId: Int): Flow<MovieDescription?>

    suspend fun updateMovieFavStatus(movieId: Int)

    suspend fun saveMovieDescription(movieDescription: MovieDescription)
}