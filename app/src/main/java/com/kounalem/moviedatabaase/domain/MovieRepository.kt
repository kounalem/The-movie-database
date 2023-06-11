package com.kounalem.moviedatabaase.domain

import com.kounalem.moviedatabaase.domain.models.Movie
import com.kounalem.moviedatabaase.domain.models.MovieDescription
import com.kounalem.moviedatabaase.domain.models.PopularMovies
import com.kounalem.moviedatabaase.util.Resource
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    fun nowPlaying(pageNo: Int): Flow<Resource<PopularMovies>>

    fun search(query: String): Flow<Resource<List<Movie>>>

    suspend fun getMovieById(id: Int): Resource<MovieDescription>
    suspend fun favouriteAction(id: Int, favouriteAction: Boolean)
}