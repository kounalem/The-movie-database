package com.kounalem.moviedatabase.data.db

import com.kounalem.moviedatabase.data.db.mapper.DescriptionDataMapper
import com.kounalem.moviedatabase.data.db.mapper.MovieMapper
import com.kounalem.moviedatabase.data.db.mapper.PopularMoviesMapper
import com.kounalem.moviedatabase.data.db.models.RoomMovieDescription
import com.kounalem.moviedatabase.domain.models.Movie
import com.kounalem.moviedatabase.domain.models.MovieDescription
import com.kounalem.moviedatabase.domain.models.PopularMovies

class LocalDataSource(
    private val dao: MovieDao,
    private val movieMapper: MovieMapper,
    private val descriptionDataMapper: DescriptionDataMapper,
    private val popularMoviesMapper: PopularMoviesMapper,
) {
    suspend fun nowPlaying(): List<PopularMovies> {
        return dao.nowPlaying().map { popularMoviesMapper.mapToDomain(it) }
    }

    suspend fun getFilteredMovies(query: String): List<Movie> {
        return dao.getFilteredMovies(query).map {
            movieMapper.mapToDomain(it)
        }
    }

    suspend fun nowPlaying(pageNo: Int): PopularMovies {
        return popularMoviesMapper.mapToDomain(dao.nowPlaying(pageNo))
    }

    suspend fun getMovieDescriptionById(movieId: Int): MovieDescription? {
        val data: RoomMovieDescription? = dao.getMovieDescriptionById(movieId)
        return data?.let {
            descriptionDataMapper.mapToDomain(it)
        }
    }

    suspend fun saveMovie(popularMovies: PopularMovies) {
        dao.saveMovie(popularMoviesMapper.mapToRoom(popularMovies))
        popularMovies.movies.map {
            saveMovie(it)
        }
    }

    private suspend fun saveMovie(movie: Movie) = dao.saveMovie(movieMapper.mapToRoom(movie))

    suspend fun saveMovieDescription(movieDescription: MovieDescription) =
        dao.saveMovieDescription(descriptionDataMapper.mapToRoom(movieDescription))

}