package com.kounalem.moviedatabase.database.movie

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kounalem.moviedatabase.database.movie.models.TvShowEntity
import kotlinx.coroutines.flow.Flow

@Dao
internal interface TvShowsDao {
    @Query("SELECT * FROM tvshow")
    fun getAllShows(): Flow<List<TvShowEntity>>

    @Query("SELECT * FROM tvshow WHERE id=:showId LIMIT 1")
    fun getShowDescriptionById(showId: Int): Flow<TvShowEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveShow(movie: TvShowEntity)

    @Query("UPDATE tvshow SET isFavourite = NOT isFavourite WHERE id = :showId")
    suspend fun updateShowFavStatus(showId: Int)

    @Query("SELECT * FROM tvshow WHERE LOWER(title) LIKE '%' || LOWER(:query) || '%' ORDER BY firstAirDate ASC")
    fun getFilteredShows(query: String): Flow<List<TvShowEntity>>

    @Query("DELETE FROM tvshow")
    suspend fun deleteAll()
}
