package com.kounalem.moviedatabase

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.kounalem.moviedatabase.core.data.NetworkMonitor
import dagger.Lazy
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApplicationLifecycleCallbacks @Inject constructor(
    private val networkMonitor: Lazy<NetworkMonitor>,
    @param:com.kounalem.moviedatabase.shared.annotation.Application private val appScope: CoroutineScope,
) : DefaultLifecycleObserver {

    override fun onStart(owner: LifecycleOwner) {}
}
