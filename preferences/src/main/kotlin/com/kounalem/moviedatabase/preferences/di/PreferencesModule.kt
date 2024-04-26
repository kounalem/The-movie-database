package com.kounalem.moviedatabase.preferences.di

import android.content.Context
import com.kounalem.moviedatabase.preferences.PreferenceRepository
import com.kounalem.moviedatabase.preferences.PreferenceRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PreferencesModule {

    @Provides
    fun provideApplicationContext(@ApplicationContext context: Context): Context {
        return context
    }

    @Provides
    @Singleton
    internal fun provideServerDataSource(
        @ApplicationContext context: Context,
        @SharedPrefsFile sharedPrefsFileName: String,
        @SecureSharedPrefsFile securePrefsFileName: String,
    ): PreferenceRepository = PreferenceRepositoryImpl(context = context, sharedPrefsFileName, securePrefsFileName)
}

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class SharedPrefsFile

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class SecureSharedPrefsFile

@Module
@InstallIn(SingletonComponent::class)
object SharedPrefsNameModule {
    @Provides
    @SharedPrefsFile
    fun provideSharedPrefsFile(): String = "com.kounalem.moviedatabase.prefs"

    @Provides
    @SecureSharedPrefsFile
    fun provideSecureSharedPrefsFile(): String = "moviedatabasesec"
}
