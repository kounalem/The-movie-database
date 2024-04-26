package com.kounalem.moviedatabase.debug

import android.app.Activity

interface DevMenuSensorDelegate {
    fun registerShakeListener(activity: Activity)
    fun removeShakeListener()
}