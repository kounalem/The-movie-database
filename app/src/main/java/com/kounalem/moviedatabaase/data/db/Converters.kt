package com.kounalem.moviedatabaase.data.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kounalem.moviedatabaase.data.db.models.MovieDAO
import com.kounalem.moviedatabaase.data.db.models.MovieDescriptionDAO
import com.kounalem.moviedatabaase.data.db.models.PopularMoviesDAO


class MovieDescriptionConverters {
    @TypeConverter
    fun fromString(value: String): ArrayList<MovieDescriptionDAO> {
        val listType = object : TypeToken<ArrayList<String>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromArrayList(list: ArrayList<MovieDescriptionDAO>): String {
        return Gson().toJson(list)
    }
}

class PopularMoviesConverters {
    @TypeConverter
    fun fromString(value: String): ArrayList<PopularMoviesDAO> {
        val listType = object : TypeToken<PopularMoviesDAO>() {}.type

        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromArrayList(list: PopularMoviesDAO): String {
        return Gson().toJson(list)
    }
}

class MovieConverters {
    @TypeConverter
    fun fromString(value: String): ArrayList<MovieDAO> {
        val listType = object : TypeToken<ArrayList<MovieDAO>>() {}.type

        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromArrayList(list: ArrayList<MovieDAO>): String {
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
