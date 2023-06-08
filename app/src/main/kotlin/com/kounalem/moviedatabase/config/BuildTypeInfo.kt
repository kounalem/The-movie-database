package com.kounalem.moviedatabase.config

interface BuildTypeInfo {
    fun isDev(): Boolean
    fun isDebug(): Boolean
}
