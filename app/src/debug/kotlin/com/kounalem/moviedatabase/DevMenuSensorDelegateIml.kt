/*
 * Created by Emmanouil Kounalakis on 22.09.2021.
 * Copyright (c) 2021 Muzz ltd. All rights reserved.
 */
package com.kounalem.moviedatabase

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import com.kounalem.moviedatabase.debug.DevMenuSensorDelegate
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlin.math.sqrt
class DevMenuSensorDelegateIml @Inject constructor(
    @ApplicationContext context: Context,
) : DevMenuSensorDelegate {
    private var mSensorManager: SensorManager? = null
    private var gravity: Triple<Double, Double, Double> = Triple(0.0, 0.0, 0.0)
    private var shakeSensorListener: SensorEventListener? = null

    init {
        mSensorManager =
            context.getSystemService(AppCompatActivity.SENSOR_SERVICE) as SensorManager
    }

    private var openedDevConfig = false
    override fun registerShakeListener(activity: Activity) {
        shakeSensorListener?.let { mSensorManager?.unregisterListener(shakeSensorListener) }
        shakeSensorListener = object : SensorEventListener {
            override fun onSensorChanged(se: SensorEvent) {
                val x = se.values[0] / se.accuracy.coerceAtLeast(1)
                val y = se.values[1] / se.accuracy.coerceAtLeast(1)
                val z = se.values[2] / se.accuracy.coerceAtLeast(1)
                // see comment in SensorEvent
                val alpha = 0.8
                gravity = Triple(
                    alpha * gravity.first + (1 - alpha) * x,
                    alpha * gravity.second + (1 - alpha) * y,
                    alpha * gravity.third + (1 - alpha) * z,
                )
                val a = Triple(
                    x - gravity.first,
                    y - gravity.second,
                    z - gravity.third,
                )
                val accelerationMagnitude =
                    sqrt(a.first * a.first + a.second * a.second + a.third * a.third)

                // Magnitude is in m/s^2
                if (accelerationMagnitude > 10) {
                    openedDevConfig = true
                    activity.startActivity(Intent(activity, DevActivity::class.java))
                }
            }

            override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
        }

        mSensorManager?.registerListener(
            shakeSensorListener,
            mSensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
            SensorManager.SENSOR_DELAY_NORMAL,
        )
    }

    override fun removeShakeListener() {
        shakeSensorListener?.let {
            mSensorManager?.unregisterListener(shakeSensorListener)
        }
        shakeSensorListener = null
        openedDevConfig = false
    }
}
