package com.kounalem.moviedatabase.di

import androidx.lifecycle.DefaultLifecycleObserver
import com.kounalem.moviedatabase.ApplicationLifecycleCallbacks
import com.kounalem.moviedatabase.config.BuildTypeInfo
import com.kounalem.moviedatabase.utils.BuildTypeInfoImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {
    @Binds
    abstract fun bindPopularMoviesFeatureFlags(impl: BuildTypeInfoImpl): BuildTypeInfo

    @Binds
    @IntoSet
    abstract fun bindApplicationLifecycleCallbacks(
        impl: ApplicationLifecycleCallbacks,
    ): DefaultLifecycleObserver
}