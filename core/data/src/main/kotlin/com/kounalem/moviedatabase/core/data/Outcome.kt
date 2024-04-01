package com.kounalem.moviedatabase.core.data

sealed class Outcome<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T?): Outcome<T>(data)
    class Error<T>(message: String, data: T? = null): Outcome<T>(data, message)
}
