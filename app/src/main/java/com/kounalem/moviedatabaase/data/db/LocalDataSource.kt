package com.kounalem.moviedatabaase.data.db

import com.kounalem.moviedatabaase.data.db.models.MovieDAO
import com.kounalem.moviedatabaase.data.db.models.MovieDescriptionDAO
import com.kounalem.moviedatabaase.data.db.models.PopularMoviesDAO
import javax.inject.Inject

class LocalDataSource @Inject constructor(private val movieDao: MovieDao) {

    suspend fun getMovieDescriptionById(movieId: Int): MovieDescriptionDAO =
        movieDao.getMovieDescriptionById(movieId)

    suspend fun search(query: String): List<MovieDAO> = movieDao.search(query)

    suspend fun saveMovie(movie: MovieDAO) = movieDao.saveMovie(movie)

    suspend fun saveMovie(movie: PopularMoviesDAO) = movieDao.saveMovie(movie)

    suspend fun nowPlaying(): List<PopularMoviesDAO> = movieDao.nowPlaying()

    suspend fun saveMovieDescription(movieDescription: MovieDescriptionDAO) =
        movieDao.saveMovieDescription(movieDescription)

}