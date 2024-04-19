package com.kounalem.moviedatabase.repository

import com.kounalem.moviedatabase.domain.models.TvShow
import kotlinx.coroutines.flow.Flow

interface TvShowRepository {
    val tvShows: Flow<Outcome<List<TvShow>>>
    fun getServerTvShows(pageNo: Int)
    fun search(query: String): Flow<List<TvShow>>
    fun getTvShowByIdObs(id: Int = -1): Flow<Outcome<TvShow>>
    suspend fun updateTvShowFavStatus(id: Int)
    fun getAllLocalSavedShows(): Flow<List<TvShow>>
}