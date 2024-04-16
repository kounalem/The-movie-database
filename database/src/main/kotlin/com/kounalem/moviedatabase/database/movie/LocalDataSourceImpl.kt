package com.kounalem.moviedatabase.database.movie

import com.kounalem.moviedatabase.database.movie.mapper.mapToDomain
import com.kounalem.moviedatabase.database.movie.mapper.mapToEntity
import com.kounalem.moviedatabase.domain.models.Movie
import com.kounalem.moviedatabase.domain.models.TvShow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class LocalDataSourceImpl(
    private val daoMovies: MovieDao,
    private val daoTvShows: TvShowsDao,
) : LocalDataSource {

    override fun getFilteredMovies(query: String): Flow<List<Movie>> {
        return daoMovies.getFilteredMovies(query).map { movieEntity ->
            movieEntity.map {
                it.mapToDomain()
            }
        }
    }

    override fun getAllMovies(): Flow<List<Movie>> {
        return daoMovies.getAllMovies().map { entity ->
            entity.map { it.mapToDomain() }
        }
    }
    override fun getMovies(pageNo: Int): Flow<List<Movie>> {
        return daoMovies.getMoviesForPage(pageNo).map { entity ->
            entity.map { it.mapToDomain() }
        }
    }

    override suspend fun saveMovieList(movies: List<Movie>): List<Unit> =
        movies.map { movie -> daoMovies.saveMovie(movie.mapToEntity()) }

    override fun getMovieByIdObs(movieId: Int): Flow<Movie> =
        daoMovies.getMovieByIdObs(movieId).map {it.mapToDomain()  }

    override suspend fun updateMovieFavStatus(movieId: Int) =
        daoMovies.updateMovieFavStatus(movieId)

    //shows
    override fun getFilteredShows(query: String): Flow<List<TvShow>> {
        return daoTvShows.getFilteredShows(query).map { entity ->
            entity.map {
                it.mapToDomain()
            }
        }
    }

    override fun getAllShows(): Flow<List<TvShow>> {
        return daoTvShows.getAllShows().map { entity ->
            entity.map { it.mapToDomain() }
        }
    }

    override suspend fun saveShowList(movies: List<TvShow>): List<Unit> =
        movies.map { movie -> daoTvShows.saveShow(movie.mapToEntity()) }

    override fun getShowById(id: Int): Flow<TvShow?> =
        daoTvShows.getShowDescriptionById(id).map {
            it?.mapToDomain()
        }

    override suspend fun updateShowFavStatus(movieId: Int) =
        daoTvShows.updateShowFavStatus(movieId)

    override suspend fun saveShowDescription(show: TvShow) =
        daoTvShows.saveShow(show.mapToEntity())
}