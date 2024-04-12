package com.kounalem.moviedatabase.database.movie

import com.kounalem.moviedatabase.domain.models.Movie
import com.kounalem.moviedatabase.domain.models.MovieDescription
import com.kounalem.moviedatabase.domain.models.TvShow
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
    fun getFilteredMovies(query: String): Flow<List<Movie>>

    fun getAllMovies(): Flow<List<Movie>>

    suspend fun saveMovieList(movies: List<Movie>): List<Unit>

    fun getMovieDescriptionById(movieId: Int): Flow<MovieDescription?>

    suspend fun updateMovieFavStatus(movieId: Int)

    suspend fun saveMovieDescription(movieDescription: MovieDescription)

    fun getFilteredShows(query: String): Flow<List<TvShow>>
    fun getAllShows(): Flow<List<TvShow>>
    suspend fun saveShowList(movies: List<TvShow>): List<Unit>
    fun getShowById(id: Int): Flow<TvShow?>
    suspend fun updateShowFavStatus(movieId: Int)
    suspend fun saveShowDescription(show: TvShow)
}