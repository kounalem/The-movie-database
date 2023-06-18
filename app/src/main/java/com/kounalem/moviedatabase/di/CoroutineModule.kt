package com.kounalem.moviedatabase.di

import com.kounalem.moviedatabase.util.IO
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
@InstallIn(SingletonComponent::class)
internal object CoroutineModule {

    @Provides
    @IO
    fun providesIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

}
