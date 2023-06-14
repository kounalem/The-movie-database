package com.kounalem.moviedatabase.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kounalem.moviedatabase.data.db.models.MovieEntity
import com.kounalem.moviedatabase.data.db.models.MovieDescriptionEntity

@Database(
    entities = [
        MovieEntity::class, MovieDescriptionEntity::class
    ], version = 1,
)
@TypeConverters(value = [(MovieDescriptionConverters::class), (MovieConverters::class), (IntConverters::class)])
abstract class AppDatabase : RoomDatabase() {
    abstract val movieDao: MovieDao

    companion object {
        const val NAME = "moviedb.db"
    }
}