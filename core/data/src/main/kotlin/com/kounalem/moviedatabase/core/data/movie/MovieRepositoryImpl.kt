package com.kounalem.moviedatabase.core.data.movie

import com.kounalem.moviedatabase.database.movie.LocalDataSource
import com.kounalem.moviedatabase.domain.models.Movie
import com.kounalem.moviedatabase.network.NetworkResponse
import com.kounalem.moviedatabase.network.movies.MoviesDataSource
import com.kounalem.moviedatabase.repository.MovieRepository
import com.kounalem.moviedatabase.repository.Outcome
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

internal class MovieRepositoryImpl
    @Inject
    constructor(
        private val server: MoviesDataSource,
        private val local: LocalDataSource,
        private val coroutineScope: CoroutineScope,
    ) : MovieRepository {
        private val pageFlow: MutableSharedFlow<Int> = MutableSharedFlow(replay = 1)

        private val errors: MutableStateFlow<String?> = MutableStateFlow(null)
        private val _movies = MutableStateFlow(mutableListOf<Movie>())

        override val movies: Flow<Outcome<List<Movie>>>
            get() =
                combine(_movies, errors) { result, e ->
                    if (result.isEmpty() && !e.isNullOrEmpty()) {
                        Outcome.Error(e)
                    } else {
                        Outcome.Success(result)
                    }
                }

        init {
            pageFlow.flatMapLatest { pageNo ->
                local.getMovies(pageNo = pageNo).distinctUntilChanged().map { movies ->
                    if (movies.isNotEmpty()) {
                        _movies.value = movies.toMutableList()
                    } else {
                        getNextMovies(pageNo = pageNo)
                    }
                }
            }.launchIn(coroutineScope)
        }

        override fun search(query: String): Flow<List<Movie>> = local.getFilteredMovies(query).distinctUntilChanged()

        private fun getNextMovies(pageNo: Int) {
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

        override fun getMoviesForPage(pageNo: Int) {
            pageFlow.tryEmit(pageNo)
        }

        override fun getMovieById(id: Int): Flow<Outcome<Movie>> =
            local.getMovieByIdObs(id).map {
                Outcome.Success(it) as Outcome<Movie>
            }.catch { e ->
                emit(Outcome.Exception(e.cause ?: Throwable("Could not find movie")))
            }

        override suspend fun updateMovieFavStatus(id: Int) {
            local.updateMovieFavStatus(id)
        }

        override fun getAllLocalSavedMovies(): Flow<List<Movie>> = local.getAllMovies()
    }
