package com.kounalem.moviedatabase.database.movie

import com.kounalem.moviedatabase.database.movie.mapper.mapToDomain
import com.kounalem.moviedatabase.database.movie.mapper.mapToEntity
import com.kounalem.moviedatabase.domain.models.Movie
import com.kounalem.moviedatabase.domain.models.MovieDescription
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class LocalDataSourceImpl(
    private val dao: MovieDao,
) : LocalDataSource {

    override fun getFilteredMovies(query: String): Flow<List<Movie>> {
        return dao.getFilteredMovies(query).map { movieEntity ->
            movieEntity.map {
                it.mapToDomain()
            }
        }
    }

    override fun getAllMovies(): Flow<List<Movie>> {
        return dao.getAllMovies().map { entity ->
            entity.map { it.mapToDomain() }
        }
    }

    override suspend fun saveMovieList(movies: List<Movie>): List<Unit> =
        movies.map { movie -> dao.saveMovie(movie.mapToEntity()) }

    override fun getMovieDescriptionById(movieId: Int): Flow<MovieDescription?> =
        dao.getMovieDescriptionById(movieId).map {
            it?.mapToDomain()
        }

    override suspend fun updateMovieFavStatus(movieId: Int) = dao.updateMovieFavStatus(movieId)

    override suspend fun saveMovieDescription(movieDescription: MovieDescription) =
        dao.saveMovieDescription(movieDescription.mapToEntity())

}