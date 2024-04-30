package com.kounalem.moviedatabase.config

interface BuildTypeInfo {
    fun isDev(): Boolean
    fun isDebug(): Boolean
    fun isDebugApplicationId(): Boolean
    fun appVersion(): String
    fun deviceVersion(): String
    fun versionCode(): Int
}
