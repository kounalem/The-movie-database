package com.kounalem.moviedatabase.database.movie

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kounalem.moviedatabase.database.movie.models.MovieDescriptionEntity
import com.kounalem.moviedatabase.database.movie.models.MovieEntity
import com.kounalem.moviedatabase.database.movie.models.SeasonEntity

internal class MovieDescriptionConverters {
    @TypeConverter
    fun fromString(value: String): ArrayList<MovieDescriptionEntity> {
        val listType = object : TypeToken<ArrayList<String>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromArrayList(list: ArrayList<MovieDescriptionEntity>): String {
        return Gson().toJson(list)
    }
}

internal class MovieConverters {
    @TypeConverter
    fun fromString(value: String): ArrayList<MovieEntity> {
        val listType = object : TypeToken<ArrayList<MovieEntity>>() {}.type

        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromArrayList(list: ArrayList<MovieEntity>): String {
        return Gson().toJson(list)
    }
}

internal class IntConverters {
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


class ListStringConverter {

    @TypeConverter
    fun fromListString(value: List<String>?): String? {
        return value?.joinToString(",")
    }

    @TypeConverter
    fun toListString(value: String?): List<String>? {
        return value?.split(",")?.map { it.trim() }
    }
}

class SeasonListConverter {

    private val gson = Gson()

    @TypeConverter
    fun fromSeasonList(value: List<SeasonEntity>?): String? {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toSeasonList(value: String?): List<SeasonEntity>? {
        val listType = object : TypeToken<List<SeasonEntity>>() {}.type
        return gson.fromJson(value, listType)
    }
}