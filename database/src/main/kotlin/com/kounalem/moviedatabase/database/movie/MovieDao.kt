package com.kounalem.moviedatabase.database.movie

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kounalem.moviedatabase.database.movie.models.MovieDescriptionEntity
import com.kounalem.moviedatabase.database.movie.models.MovieEntity
import kotlinx.coroutines.flow.Flow

@Dao
internal interface MovieDao {

    @Query("SELECT * FROM movie")
    fun getAllMovies(): Flow<List<MovieEntity>>

    @Query("SELECT * FROM movie_description WHERE id=:movieId LIMIT 1")
    fun getMovieDescriptionById(movieId: Int): Flow<MovieDescriptionEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveMovie(movie: MovieEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveMovieDescription(movieDescription: MovieDescriptionEntity)

    @Query("UPDATE movie_description SET isFavourite = NOT isFavourite WHERE id = :movieId")
    suspend fun updateMovieFavStatus(movieId: Int)

    @Query("SELECT * FROM movie WHERE LOWER(title) LIKE '%' || LOWER(:query) || '%' ORDER BY date ASC")
    fun getFilteredMovies(query: String): Flow<List<MovieEntity>>
}