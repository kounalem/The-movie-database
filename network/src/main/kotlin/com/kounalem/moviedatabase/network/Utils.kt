package com.kounalem.moviedatabase.network

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response

sealed interface NetworkResponse<T> {
    data class Success<R>(
        val body: R,
    ) : NetworkResponse<R>

    data class Error<T>(val code: Int, val message: String) : NetworkResponse<T>

    @JvmInline
    value class Exception<T>(val exception: Throwable) : NetworkResponse<T>
}

private fun <T : Any, R> Response<T>.toNetworkResponse(mapper: (T) -> R): NetworkResponse<R> {
    return try {
        if (isSuccessful) {
            val body = body()!!
            NetworkResponse.Success(mapper(body))
        } else {
            NetworkResponse.Error(code(), message())
        }
    } catch (e: Exception) {
        NetworkResponse.Exception(e)
    }
}

internal fun <T : Any, R> wrapServiceCall(
    call: suspend () -> Response<T>,
    mapper: (T) -> R
): Flow<NetworkResponse<R>> {
    return flow {
        try {
            val response = call()
            emit(response.toNetworkResponse(mapper))
        } catch (e: Exception) {
            emit(NetworkResponse.Exception(e))
        }
    }
}

