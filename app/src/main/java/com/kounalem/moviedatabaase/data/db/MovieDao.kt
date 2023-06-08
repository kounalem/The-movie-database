package com.kounalem.moviedatabaase.data.db

import androidx.room.*
import com.kounalem.moviedatabaase.data.db.models.MovieDAO
import com.kounalem.moviedatabaase.data.db.models.MovieDescriptionDAO
import com.kounalem.moviedatabaase.data.db.models.PopularMoviesDAO

@Dao
interface MovieDao {

    @Query("SELECT * FROM popular_movies")
    suspend fun nowPlaying(): List<PopularMoviesDAO>

    @Query("SELECT * FROM movie WHERE id=:movieId")
    suspend fun getMovieById(movieId: Int): MovieDAO

    @Query("SELECT * FROM movie_description WHERE id=:movieId")
    suspend fun getMovieDescriptionById(movieId: Int): MovieDescriptionDAO

    @Query("SELECT * FROM movie WHERE LOWER(title) LIKE '%' || LOWER(:query)  || '%' ORDER BY title")
    suspend fun search(query: String): List<MovieDAO>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveMovie(popularMovies: PopularMoviesDAO)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveMovie(movie: MovieDAO)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveMovieDescription(movieDescription: MovieDescriptionDAO)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun saveAllMovies(movies: List<MovieDAO>)

    @Delete
    suspend fun removeMovie(movie: MovieDAO)

    @Query("DELETE FROM movie")
    suspend fun clear()
}