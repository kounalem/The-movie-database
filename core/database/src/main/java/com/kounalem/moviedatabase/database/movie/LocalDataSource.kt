package com.kounalem.moviedatabase.database.movie

import com.kounalem.moviedatabase.domain.models.Movie
import com.kounalem.moviedatabase.domain.models.TvShow
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
    fun getFilteredMovies(query: String): Flow<List<Movie>>

    fun getAllMovies(): Flow<List<Movie>>

    suspend fun clearAllMovies()

    fun getMovies(pageNo: Int): Flow<List<Movie>>

    suspend fun saveMovieList(movies: List<Movie>): List<Unit>

    suspend fun updateMovieFavStatus(movieId: Int)

    fun getMovieByIdObs(movieId: Int): Flow<Movie>

    fun getFilteredShows(query: String): Flow<List<TvShow>>

    fun getAllShows(): Flow<List<TvShow>>

    suspend fun saveShowList(movies: List<TvShow>): List<Unit>

    fun getShowById(id: Int): Flow<TvShow?>

    suspend fun updateShowFavStatus(movieId: Int)

    suspend fun saveShowDescription(show: TvShow)
    suspend fun clearAllShows()
}
