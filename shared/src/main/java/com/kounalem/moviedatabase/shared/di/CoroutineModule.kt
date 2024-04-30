package com.kounalem.moviedatabase.shared.di

import com.kounalem.moviedatabase.shared.annotation.Application
import com.kounalem.moviedatabase.shared.annotation.Default
import com.kounalem.moviedatabase.shared.annotation.IO
import com.kounalem.moviedatabase.shared.annotation.Idle
import com.kounalem.moviedatabase.shared.annotation.Main
import com.kounalem.moviedatabase.shared.annotation.SingleIO
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.launch
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object CoroutineModule {

    @Provides
    @Default
    fun provideDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default

    @OptIn(ExperimentalCoroutinesApi::class)
    @Provides
    @SingleIO
    fun providesSingleIoDispatcher(): CoroutineDispatcher = Dispatchers.IO.limitedParallelism(1)

    @Provides
    @IO
    fun providesIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @Idle
    fun providesIdleDispatcher(): CoroutineDispatcher = Dispatchers.Default

    @Provides
    @Main
    fun providesMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @Singleton
    @Provides
    @Application
    fun providesApplicationScope(
        @IO ioDispatcher: CoroutineDispatcher,
    ): CoroutineScope = CoroutineScope(SupervisorJob() + ioDispatcher).also {
        it.launch {
            awaitCancellation() // Suspends to keep getting updates until cancellation.
        }
    }
}
