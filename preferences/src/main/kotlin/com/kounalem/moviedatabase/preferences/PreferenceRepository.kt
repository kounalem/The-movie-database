package com.kounalem.moviedatabase.preferences

import kotlinx.coroutines.flow.Flow

/**
 * Repository to store generic preference data.
 * The keys to this data are not managed by this.
 */
interface PreferenceRepository {
    fun getBoolean(key: String, default: Boolean = false): Boolean
    fun getString(key: String, default: String = ""): String
    fun getInt(key: String, default: Int = -1): Int
    fun getLong(key: String, default: Long = -1L): Long
    fun getStringSet(key: String, default: Set<String> = emptySet()): Set<String>

    fun flowBoolean(
        key: String,
        default: Boolean = false,
        emitImmediately: Boolean = true,
    ): Flow<Boolean>

    fun flowString(
        key: String,
        default: String = "",
        emitImmediately: Boolean = true,
    ): Flow<String>

    fun flowInt(
        key: String,
        default: Int = 0,
        emitImmediately: Boolean = true,
    ): Flow<Int>

    fun flowLong(
        key: String,
        default: Long = 0L,
        emitImmediately: Boolean = true,
    ): Flow<Long>

    fun flowStringSet(
        key: String,
        default: Set<String> = emptySet(),
        emitImmediately: Boolean = true,
    ): Flow<Set<String>>

    fun insert(key: String, value: Any?)
    fun remove(key: String)
    fun removeEntry(prefName: String)

    companion object {
        const val ENVIRONMENT = "ENVIRONMENT"
    }
}
