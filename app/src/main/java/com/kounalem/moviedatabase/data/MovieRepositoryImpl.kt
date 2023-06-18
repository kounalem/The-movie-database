package com.kounalem.moviedatabase.data

import com.kounalem.moviedatabase.data.db.LocalDataSource
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
) : MovieRepository {
    override fun nowPlaying(pageNo: Int): Flow<Resource<PopularMovies>> {
        return flow {
            emit(Resource.Loading())
            try {
                val serverInfo: PopularMovies = serverDataSource.nowPlaying(pageNo)
                localDataSource.saveMovie(serverInfo)
                emit(Resource.Success(serverInfo))
            } catch (e: Exception) {
                try {
                    val localInfo: PopularMovies = localDataSource.nowPlaying(pageNo = pageNo)
                    emit(Resource.Success(localInfo))
                } catch (e: Exception) {
                    emit(Resource.Error("Couldn't load data"))
                }
            }
        }
    }

    override fun search(query: String): Flow<Resource<List<Movie>>> {
        return flow {
            emit(Resource.Loading())
            try {
                val movieList: List<Movie> = localDataSource.getFilteredMovies(query)
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
                Resource.Success(dbInfo)
            } else {
                val serverInfo = serverDataSource.getMovieById(id)
                localDataSource.saveMovieDescription(serverInfo)

                Resource.Success(serverInfo)
            }

        } catch (e: Exception) {
            Resource.Error(e.localizedMessage.orEmpty())
        }
    }

    override suspend fun favouriteAction(id: Int, favouriteAction: Boolean) {
        val localInfo =
            localDataSource.getMovieDescriptionById(id)?.copy(isFavourite = favouriteAction)
        localInfo?.let {
            localDataSource.saveMovieDescription(it)
        }
    }

}