package com.kounalem.moviedatabase.database.movie

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kounalem.moviedatabase.database.movie.models.MovieDescriptionEntity
import com.kounalem.moviedatabase.database.movie.models.MovieEntity

@Database(entities = [MovieEntity::class, MovieDescriptionEntity::class], version = 1)
@TypeConverters(value = [(MovieDescriptionConverters::class), (MovieConverters::class), (IntConverters::class)])
internal abstract class AppDatabase : RoomDatabase() {
    abstract val movieDao: MovieDao

    companion object {
        const val NAME = "moviedb.db"
    }
}