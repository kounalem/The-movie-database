package com.kounalem.moviedatabase.network

sealed interface NetworkResponse<T> {
    data class Success<R>(
        val body: R,
    ) : NetworkResponse<R>

    data class Error<T>(val code: Int, val message: String) : NetworkResponse<T>

    @JvmInline
    value class Exception<T>(val exception: Throwable) : NetworkResponse<T>
}
