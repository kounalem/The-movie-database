package com.kounalem.moviedatabase.domain

import com.kounalem.moviedatabase.domain.models.Movie
import com.kounalem.moviedatabase.domain.models.MovieDescription
import com.kounalem.moviedatabase.domain.models.PopularMovies
import com.kounalem.moviedatabase.util.Resource
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    fun nowPlaying(pageNo: Int): Flow<Resource<PopularMovies>>

    fun search(query: String): Flow<Resource<List<Movie>>>

    suspend fun getMovieById(id: Int): Resource<MovieDescription>
    suspend fun favouriteAction(id: Int, favouriteAction: Boolean)
}