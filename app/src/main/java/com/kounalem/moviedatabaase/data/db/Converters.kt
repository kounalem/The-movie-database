package com.kounalem.moviedatabaase.data.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kounalem.moviedatabaase.data.db.models.RoomMovie
import com.kounalem.moviedatabaase.data.db.models.RoomMovieDescription
import com.kounalem.moviedatabaase.data.db.models.RoomPopularMovies


class MovieDescriptionConverters {
    @TypeConverter
    fun fromString(value: String): ArrayList<RoomMovieDescription> {
        val listType = object : TypeToken<ArrayList<String>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromArrayList(list: ArrayList<RoomMovieDescription>): String {
        return Gson().toJson(list)
    }
}

class PopularMoviesConverters {
    @TypeConverter
    fun fromString(value: String): ArrayList<RoomPopularMovies> {
        val listType = object : TypeToken<RoomPopularMovies>() {}.type

        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromArrayList(list: RoomPopularMovies): String {
        return Gson().toJson(list)
    }
}

class MovieConverters {
    @TypeConverter
    fun fromString(value: String): ArrayList<RoomMovie> {
        val listType = object : TypeToken<ArrayList<RoomMovie>>() {}.type

        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromArrayList(list: ArrayList<RoomMovie>): String {
        return Gson().toJson(list)
    }
}

class IntConverters {
    @TypeConverter
    fun fromString(value: String): ArrayList<Int> {
        val listType = object : TypeToken<ArrayList<Int>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromArrayList(list: ArrayList<Int>): String {
        return Gson().toJson(list)
    }
}
