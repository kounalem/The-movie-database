package com.kounalem.moviedatabase

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.kounalem.moviedatabase.core.data.NetworkMonitor
import dagger.Lazy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApplicationLifecycleCallbacks @Inject constructor(
    //@ApplicationContext private val context: Context,
    // analytics
    private val networkMonitor: Lazy<NetworkMonitor>,
    // config
    // private val sharedPreferences: Lazy<PreferenceRepository>,
    @com.kounalem.moviedatabase.shared.annotation.Application private val appScope: CoroutineScope,
) : DefaultLifecycleObserver {

    override fun onStart(owner: LifecycleOwner) {

        networkMonitor.get().isOnline
            .map(Boolean::not).onEach {
                //use it for analytics or something
            }
            .stateIn(
                scope = appScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = false,
            )

    }

    override fun onStop(owner: LifecycleOwner) {

    }
}
