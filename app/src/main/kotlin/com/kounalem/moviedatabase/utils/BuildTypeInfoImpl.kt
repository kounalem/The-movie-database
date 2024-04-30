package com.kounalem.moviedatabase.utils

import android.os.Build
import com.kounalem.moviedatabase.BuildConfig
import com.kounalem.moviedatabase.config.BuildTypeInfo
import javax.inject.Inject

class BuildTypeInfoImpl @Inject constructor() : BuildTypeInfo {

    override fun isDev() = BuildConfig.FLAVOR.contains("alpha", ignoreCase = true)

    override fun isDebug() = BuildConfig.DEBUG

    override fun isDebugApplicationId(): Boolean = BuildConfig.APPLICATION_ID.contains("debug")

    override fun appVersion(): String = BuildConfig.APP_VERSION
    override fun deviceVersion(): String = Build.VERSION.RELEASE
    override fun versionCode(): Int = BuildConfig.VERSION_CODE
}
