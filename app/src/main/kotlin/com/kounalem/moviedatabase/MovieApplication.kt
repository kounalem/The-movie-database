package com.kounalem.moviedatabase

import android.app.Application
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.ProcessLifecycleOwner
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class MovieApplication : Application() {

    @Inject
    lateinit var applicationLifecycleCallbacks: MutableSet<DefaultLifecycleObserver>
    override fun onCreate() {
        super.onCreate()
        applicationLifecycleCallbacks.forEach {
            ProcessLifecycleOwner.get().lifecycle.addObserver(it)
        }
    }
}
