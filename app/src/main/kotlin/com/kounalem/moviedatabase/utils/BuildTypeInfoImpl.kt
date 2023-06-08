package com.kounalem.moviedatabase.utils

import com.kounalem.moviedatabase.BuildConfig
import com.kounalem.moviedatabase.config.BuildTypeInfo
import javax.inject.Inject

class BuildTypeInfoImpl @Inject constructor() : BuildTypeInfo {
    override fun isDev() = BuildConfig.FLAVOR.contains("dev", ignoreCase = true)
    override fun isDebug() = BuildConfig.DEBUG
}
