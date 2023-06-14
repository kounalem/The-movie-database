package com.kounalem.moviedatabase.data.db

import com.kounalem.moviedatabase.data.db.mapper.mapToDomain
import com.kounalem.moviedatabase.data.db.mapper.mapToEntity
import com.kounalem.moviedatabase.domain.models.Movie
import com.kounalem.moviedatabase.domain.models.MovieDescription
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocalDataSource(
    private val dao: MovieDao,
) {

    fun getFilteredMovies(query: String): Flow<List<Movie>> {
        return dao.getFilteredMovies(query).map { movieEntity ->
            movieEntity.map {
                it.mapToDomain()
            }
        }
    }

    fun getAllMovies(): Flow<List<Movie>> {
        return dao.getAllMovies().map { entity ->
            entity.map { it.mapToDomain() }
        }
    }

    suspend fun saveMovieList(movies: List<Movie>) =
        movies.map { movie -> dao.saveMovie(movie.mapToEntity()) }

    fun getMovieDescriptionById(movieId: Int): Flow<MovieDescription?> =
        dao.getMovieDescriptionById(movieId).map {
            it?.mapToDomain()
        }

    suspend fun updateMovieFavStatus(movieId: Int) = dao.updateMovieFavStatus(movieId)

    suspend fun saveMovieDescription(movieDescription: MovieDescription) =
        dao.saveMovieDescription(movieDescription.mapToEntity())

}