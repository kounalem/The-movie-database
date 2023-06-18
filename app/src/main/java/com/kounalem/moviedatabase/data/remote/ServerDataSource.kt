package com.kounalem.moviedatabase.data.remote

import com.kounalem.moviedatabase.data.remote.mapper.mappers.MovieDescriptionMapper
import com.kounalem.moviedatabase.data.remote.mapper.mappers.PopularMoviesMapper
import com.kounalem.moviedatabase.domain.models.MovieDescription
import com.kounalem.moviedatabase.domain.models.PopularMovies
import javax.inject.Inject

class ServerDataSource @Inject constructor(
    private val service: MoviesApiService,
    private val popularMoviesMapper: PopularMoviesMapper,
    private val descriptionMapper: MovieDescriptionMapper
) {
    suspend fun getMovieById(id: Int): MovieDescription {
        val movie = service.getMovieById(id)
        return descriptionMapper.map(movie)
    }

    suspend fun nowPlaying(pageNo: Int): PopularMovies {
        val nowPlaying = service.nowPlaying(pageNo)
        return popularMoviesMapper.map(nowPlaying)
    }

}