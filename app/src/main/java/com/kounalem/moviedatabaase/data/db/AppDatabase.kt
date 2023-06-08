package com.kounalem.moviedatabaase.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kounalem.moviedatabaase.data.db.models.MovieDAO
import com.kounalem.moviedatabaase.data.db.models.MovieDescriptionDAO
import com.kounalem.moviedatabaase.data.db.models.PopularMoviesDAO

@Database(
    entities = [
        MovieDAO::class, MovieDescriptionDAO::class, PopularMoviesDAO::class
    ], version = 1,
)
@TypeConverters(value = [(MovieDescriptionConverters::class), (MovieConverters::class), (IntConverters::class), (PopularMoviesConverters::class)])
abstract class AppDatabase : RoomDatabase() {
    abstract val movieDao: MovieDao

    companion object {
        const val NAME = "moviedb.db"
    }
}