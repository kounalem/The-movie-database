package com.kounalem.moviedatabase.core.data.movie

import com.kounalem.moviedatabase.repository.Outcome
import com.kounalem.moviedatabase.core.data.mapToOutcome
import com.kounalem.moviedatabase.database.movie.LocalDataSource
import com.kounalem.moviedatabase.domain.models.Movie
import com.kounalem.moviedatabase.domain.models.MovieDescription
import com.kounalem.moviedatabase.network.NetworkResponse
import com.kounalem.moviedatabase.network.movies.MoviesDataSource
import com.kounalem.moviedatabase.repository.MovieRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

internal class MovieRepositoryImpl @Inject constructor(
    private val server: MoviesDataSource,
    private val local: LocalDataSource,
    private val coroutineScope: CoroutineScope,
) : MovieRepository {

    private val errors: MutableStateFlow<String?> = MutableStateFlow(null)
    private val _movies =
        local.getAllMovies().onStart { getServerMovies(pageNo = 1) }
    override val movies: Flow<Outcome<List<Movie>>>
        get() = combine(_movies, errors) { result, e ->
            if (result.isEmpty() && !e.isNullOrEmpty()) {
                Outcome.Error(e)
            } else {
                Outcome.Success(result)
            }
        }

    override fun search(query: String): Flow<List<Movie>> =
        local.getFilteredMovies(query).distinctUntilChanged()

    override fun getServerMovies(pageNo: Int) {
        coroutineScope.launch {
            server.nowPlaying(pageNo).collect { result ->
                if (result is NetworkResponse.Success) {
                    local.saveMovieList(result.body.movies)
                    errors.value = null
                } else {
                    errors.value = "Could not retrieve movies"
                }
            }
        }
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getMovieByIdObs(id: Int): Flow<Outcome<MovieDescription>> =
        local.getMovieDescriptionById(id).flatMapLatest { description ->
            if (description != null) {
                flowOf(Outcome.Success(description))
            } else {
                server.getMovieById(id).mapToOutcome { description ->
                    coroutineScope.launch {
                        local.saveMovieDescription(description)
                    }
                    description
                }
            }
        }.catch {
            Outcome.Exception<MovieDescription>(it)
        }

    override suspend fun updateMovieFavStatus(id: Int) = local.updateMovieFavStatus(id)
}