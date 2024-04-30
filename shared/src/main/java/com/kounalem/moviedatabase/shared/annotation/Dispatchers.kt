package com.kounalem.moviedatabase.shared.annotation

import javax.inject.Qualifier

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class IO

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class Default

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
/**
 * Annotates a dispatcher used for things that have recurring delays.
 */
annotation class Idle

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class Main

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class SingleIO
