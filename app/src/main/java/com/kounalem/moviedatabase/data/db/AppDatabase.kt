package com.kounalem.moviedatabase.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kounalem.moviedatabase.data.db.models.RoomMovie
import com.kounalem.moviedatabase.data.db.models.RoomMovieDescription
import com.kounalem.moviedatabase.data.db.models.RoomPopularMovies

@Database(
    entities = [
        RoomMovie::class, RoomMovieDescription::class, RoomPopularMovies::class
    ], version = 1,
)
@TypeConverters(value = [(MovieDescriptionConverters::class), (MovieConverters::class), (IntConverters::class), (PopularMoviesConverters::class)])
abstract class AppDatabase : RoomDatabase() {
    abstract val movieDao: MovieDao

    companion object {
        const val NAME = "moviedb.db"
    }
}