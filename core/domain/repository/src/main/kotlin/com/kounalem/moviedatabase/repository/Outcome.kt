package com.kounalem.moviedatabase.repository

sealed class Outcome<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T?): Outcome<T>(data)
    class Error<T>(message: String, data: T? = null): Outcome<T>(data, message)
    class Exception<T>(val error: Throwable) : Outcome<T>()
}
