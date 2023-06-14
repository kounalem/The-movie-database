package com.kounalem.moviedatabase.data

import com.kounalem.moviedatabase.data.db.LocalDataSource
import com.kounalem.moviedatabase.data.remote.ServerDataSource
import com.kounalem.moviedatabase.domain.MovieRepository
import com.kounalem.moviedatabase.domain.models.Movie
import com.kounalem.moviedatabase.domain.models.MovieDescription
import com.kounalem.moviedatabase.util.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val server: ServerDataSource,
    private val local: LocalDataSource,
) : MovieRepository {

    private val errors: MutableStateFlow<String?> = MutableStateFlow(null)
    private val _movies =
        local.getAllMovies().onStart { getServerMovies(pageNo = 1) }
    override val movies: Flow<Resource<List<Movie>>>
        get() = combine(_movies, errors) { result, e ->
            if (result.isEmpty() && !e.isNullOrEmpty()) {
                Resource.Error(e)
            } else {
                Resource.Success(result)
            }
        }

    override fun search(query: String): Flow<List<Movie>> =
        local.getFilteredMovies(query).distinctUntilChanged()

    override suspend fun getServerMovies(pageNo: Int) {
        try {
            val popular = server.nowPlaying(pageNo)
            local.saveMovieList(popular.movies)
            errors.value = null
        } catch (_: Exception) {
            errors.value = "Could not retrieve movies"
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getMovieByIdObs(id: Int): Flow<Resource<MovieDescription>> =
        local.getMovieDescriptionById(id).flatMapLatest { description ->
            if (description != null) {
                flowOf(Resource.Success(description))
            } else {
                server.getMovieById(id).map {
                    local.saveMovieDescription(it)
                    Resource.Success(it)
                }
            }
        }.catch {
            Resource.Error<MovieDescription>("Movie info not available")
        }

    override suspend fun updateMovieFavStatus(id: Int) = local.updateMovieFavStatus(id)
}