package com.kounalem.moviedatabase.database.movie

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kounalem.moviedatabase.database.movie.models.MovieEntity
import com.kounalem.moviedatabase.database.movie.models.SeasonEntity
import com.kounalem.moviedatabase.database.movie.models.TvShowEntity

@Database(
    entities = [
        MovieEntity::class,
        TvShowEntity::class, SeasonEntity::class,
    ],
    version = 1,
)
@TypeConverters(value = [MovieConverters::class, IntConverters::class, ListStringConverter::class, SeasonListConverter::class])
internal abstract class AppDatabase : RoomDatabase() {
    abstract val movieDao: MovieDao
    abstract val tvShowDao: TvShowsDao

    companion object {
        const val NAME = "moviedb.db"
    }
}
