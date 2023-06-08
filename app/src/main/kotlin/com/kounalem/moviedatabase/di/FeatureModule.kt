package com.kounalem.moviedatabase.di

import com.kounalem.moviedatabase.feature.movies.domain.usecase.PopularMoviesFeatureFlags
import com.kounalem.moviedatabase.managers.FeatureFlags
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class FeatureModule {
    @Binds
    abstract fun bindPopularMoviesFeatureFlags(impl: FeatureFlags): PopularMoviesFeatureFlags
}