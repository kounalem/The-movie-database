package com.kounalem.moviedatabase.data

import com.kounalem.moviedatabase.data.db.LocalDataSource
import com.kounalem.moviedatabase.data.db.models.RoomMovieDescription
import com.kounalem.moviedatabase.data.mappers.MovieDataMapper
import com.kounalem.moviedatabase.data.mappers.MovieDescriptionDataMapper
import com.kounalem.moviedatabase.data.mappers.PopularMoviesDataMapper
import com.kounalem.moviedatabase.data.remote.ServerDataSource
import com.kounalem.moviedatabase.domain.MovieRepository
import com.kounalem.moviedatabase.domain.models.Movie
import com.kounalem.moviedatabase.domain.models.MovieDescription
import com.kounalem.moviedatabase.domain.models.PopularMovies
import com.kounalem.moviedatabase.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Exception
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val serverDataSource: ServerDataSource,
    private val localDataSource: LocalDataSource,
    private val movieDescriptionDataMapper: MovieDescriptionDataMapper,
    private val movieDataMapper: MovieDataMapper,
    private val popularMoviesDataMapper: PopularMoviesDataMapper,

    ) : MovieRepository {
    override fun nowPlaying(pageNo: Int): Flow<Resource<PopularMovies>> {
        return flow {
            emit(Resource.Loading())
            try {
                val serverInfo = serverDataSource.nowPlaying(pageNo)
                val localPopularMovies = popularMoviesDataMapper.map(serverInfo)
                localDataSource.saveMovie(localPopularMovies)
                val popularMovies: PopularMovies = popularMoviesDataMapper.map(localPopularMovies)
                emit(Resource.Success(popularMovies))
            } catch (e: Exception) {
                val localInfo = localDataSource.nowPlaying()
                if (localInfo.isNotEmpty())
                    localInfo.map {
                        val popularMovies: PopularMovies = popularMoviesDataMapper.map(it)
                        emit(Resource.Success(popularMovies))
                    } else {
                    emit(Resource.Error("Couldn't load data"))
                }
            }
        }
    }

    override fun search(query: String): Flow<Resource<List<Movie>>> {
        return flow {
            emit(Resource.Loading())
            try {
                val movieList: List<Movie> = localDataSource.nowPlaying()
                    .flatMap { localMovie ->
                        val list = mutableListOf<Movie>()
                        localMovie.let {
                            it.movies?.map {
                                list.add(movieDataMapper.map(it))
                            }
                        }
                        list
                    }.filter { it.title.contains(query, true) }.sortedBy {
                        it.title
                    }.distinctBy { it.id }
                emit(Resource.Success(movieList))
            } catch (e: Exception) {
                emit(Resource.Error("Couldn't load data"))
            }
        }
    }

    override suspend fun getMovieById(id: Int): Resource<MovieDescription> {
        return try {
            val dbInfo = localDataSource.getMovieDescriptionById(id)
            if (dbInfo != null) {
                Resource.Success(movieDescriptionDataMapper.map(dbInfo))
            } else {
                val serverInfo = serverDataSource.getMovieById(id)
                localDataSource.saveMovieDescription(
                    RoomMovieDescription(
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
                Resource.Success(movieDescriptionDataMapper.map(serverInfo))
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