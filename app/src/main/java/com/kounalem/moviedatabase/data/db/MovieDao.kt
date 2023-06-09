package com.kounalem.moviedatabase.data.db

import androidx.room.*
import com.kounalem.moviedatabase.data.db.models.RoomMovie
import com.kounalem.moviedatabase.data.db.models.RoomMovieDescription
import com.kounalem.moviedatabase.data.db.models.RoomPopularMovies

@Dao
interface MovieDao {

    @Query("SELECT * FROM popular_movies")
    suspend fun nowPlaying(): List<RoomPopularMovies>

    @Query("SELECT * FROM popular_movies WHERE page=:pageNo LIMIT 1")
    suspend fun nowPlaying(pageNo: Int): RoomPopularMovies

    @Query("SELECT * FROM movie WHERE id=:movieId")
    suspend fun getMovieById(movieId: Int): RoomMovie

    @Query("SELECT * FROM movie_description WHERE id=:movieId LIMIT 1")
    suspend fun getMovieDescriptionById(movieId: Int): RoomMovieDescription

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveMovie(popularMovies: RoomPopularMovies)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveMovie(movie: RoomMovie)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveMovieDescription(movieDescription: RoomMovieDescription)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun saveAllMovies(movies: List<RoomMovie>)

    @Delete
    suspend fun removeMovie(movie: RoomMovie)

    @Query("DELETE FROM movie")
    suspend fun clear()

    @Query("SELECT * FROM movie WHERE LOWER(title) LIKE '%' || LOWER(:query) || '%' ORDER BY date ASC")
    suspend fun getFilteredMovies(query: String): List<RoomMovie>
}