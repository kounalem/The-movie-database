package com.kounalem.moviedatabase

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.ui.Modifier
import com.kounalem.moviedatabase.config.BuildTypeInfo
import com.kounalem.moviedatabase.core.data.NetworkMonitor
import com.kounalem.moviedatabase.core.ui.theming.MovieDatabaseTheme
import com.kounalem.moviedatabase.debug.DevMenuSensorDelegate
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var networkMonitor: NetworkMonitor

    @Inject
    lateinit var sensorDelegate: DevMenuSensorDelegate

    @Inject
    lateinit var buidTypeInfo: BuildTypeInfo

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MovieDatabaseTheme {
                val appState =
                    rememberMovieAppState(
                        windowSizeClass = calculateWindowSizeClass(this),
                        networkMonitor = networkMonitor,
                    )
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    MovieApp(appState)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (buidTypeInfo.isDev())
            sensorDelegate.registerShakeListener(this)
    }

    override fun onPause() {
        if (buidTypeInfo.isDev())
            sensorDelegate.removeShakeListener()
        super.onPause()
    }
}
