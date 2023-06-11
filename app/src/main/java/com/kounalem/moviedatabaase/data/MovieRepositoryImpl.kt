package com.kounalem.moviedatabaase.data

import android.accounts.NetworkErrorException
import com.kounalem.moviedatabaase.data.db.LocalDataSource
import com.kounalem.moviedatabaase.data.db.models.MovieDAO
import com.kounalem.moviedatabaase.data.db.models.MovieDescriptionDAO
import com.kounalem.moviedatabaase.data.db.models.PopularMoviesDAO
import com.kounalem.moviedatabaase.data.mappers.MovieDataMapper
import com.kounalem.moviedatabaase.data.mappers.MovieDescriptionDataMapper
import com.kounalem.moviedatabaase.data.mappers.PopularMoviesDataMapper
import com.kounalem.moviedatabaase.data.remote.ServerDataSource
import com.kounalem.moviedatabaase.domain.MovieRepository
import com.kounalem.moviedatabaase.domain.models.Movie
import com.kounalem.moviedatabaase.domain.models.MovieDescription
import com.kounalem.moviedatabaase.domain.models.PopularMovies
import com.kounalem.moviedatabaase.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Exception
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val serverDataSource: ServerDataSource,
    private val localDataSource: LocalDataSource,
) : MovieRepository {

    private val movieDescriptionDataMapper by lazy { MovieDescriptionDataMapper() }
    private val movieDataMapper by lazy { MovieDataMapper() }
    private val popularMoviesDataMapper by lazy { PopularMoviesDataMapper() }

    override fun nowPlaying(pageNo: Int): Flow<Resource<PopularMovies>> {
        return flow {
            emit(Resource.Loading(true))
            try {
                val serverInfo = serverDataSource.nowPlaying(pageNo)
                val popularMoviesDao = PopularMoviesDAO(
                    page = serverInfo.page,
                    movies = serverInfo.movies?.distinctBy { it.id }?.sortedBy { it.title }
                        ?.mapTo(ArrayList()) {
                            MovieDAO(
                                originalTitle = it.originalTitle,
                                overview = it.overview,
                                posterPath = it.poster_path,
                                title = it.title,
                                voteAverage = it.voteAverage,
                                id = it.id
                            )
                        },
                    totalPages = serverInfo.totalPages,
                    totalResults = serverInfo.totalResults,
                )
                localDataSource.saveMovie(popularMoviesDao)
                popularMoviesDao.movies?.forEach {
                    localDataSource.saveMovie(it)
                }
                val popularMovies: PopularMovies = popularMoviesDataMapper.map(popularMoviesDao)
                emit(Resource.Loading(false))
                emit(Resource.Success(popularMovies))

            } catch (e: Exception) {
                try {
                    localDataSource.nowPlaying().map {
                        val popularMovies: PopularMovies = popularMoviesDataMapper.map(it)
                        emit(Resource.Loading(false))
                        emit(Resource.Success(popularMovies))
                    }
                } catch (e: Exception) {
                    emit(Resource.Error("Couldn't load data"))
                }
            }
        }
    }

    override fun search(query: String): Flow<Resource<List<Movie>>> {
        return flow {
            emit(Resource.Loading(true))
            try {
                val movieList = localDataSource.search(query)
                    .map { dtoMovie ->
                        emit(Resource.Loading(false))
                        dtoMovie.let {
                            movieDataMapper.map(it)
                        }
                    }.distinctBy { it.title }
                emit(Resource.Success(movieList))
            } catch (e: Exception) {
                emit(Resource.Loading(false))
                emit(Resource.Error("Couldn't load data"))
            }
        }
    }

    override suspend fun getMovieById(id: Int): Resource<MovieDescription> {
        return try {
            val dbInfo = localDataSource.getMovieDescriptionById(id)
            Resource.Success(movieDescriptionDataMapper.map(dbInfo))

        } catch (e: NullPointerException) {
            try {
                val serverInfo = serverDataSource.getMovieById(id)
                localDataSource.saveMovieDescription(
                    MovieDescriptionDAO(
                        id = id,
                        originalTitle = serverInfo.original_title,
                        overview = serverInfo.overview,
                        popularity = serverInfo.popularity,
                        posterPath = serverInfo.poster_path,
                        status = serverInfo.status,
                        tagline = serverInfo.tagline,
                        title = serverInfo.title,
                        voteAverage = serverInfo.vote_average,
                        isFavourite = false,
                    )
                )
                val dbInfo = localDataSource.getMovieDescriptionById(id)
                Resource.Success(movieDescriptionDataMapper.map(dbInfo))
            } catch (e: Exception) {
                Resource.Error(e.localizedMessage.orEmpty())
            }
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage.orEmpty())
        }
    }

    override suspend fun favouriteAction(id: Int, favouriteAction: Boolean) {
        val localInfo =
            localDataSource.getMovieDescriptionById(id).copy(isFavourite = favouriteAction)
        localDataSource.saveMovieDescription(localInfo)
    }

}