package com.kounalem.moviedatabaase.data.db

import com.kounalem.moviedatabaase.data.db.models.RoomMovie
import com.kounalem.moviedatabaase.data.db.models.RoomMovieDescription
import com.kounalem.moviedatabaase.data.db.models.RoomPopularMovies
import javax.inject.Inject

class LocalDataSource @Inject constructor(private val movieDao: MovieDao) {

    suspend fun getMovieDescriptionById(movieId: Int): RoomMovieDescription =
        movieDao.getMovieDescriptionById(movieId)

    suspend fun saveMovie(movie: RoomMovie) = movieDao.saveMovie(movie)

    suspend fun saveMovie(movie: RoomPopularMovies) = movieDao.saveMovie(movie)

    suspend fun nowPlaying(): List<RoomPopularMovies> = movieDao.nowPlaying()

    suspend fun saveMovieDescription(movieDescription: RoomMovieDescription) =
        movieDao.saveMovieDescription(movieDescription)

}