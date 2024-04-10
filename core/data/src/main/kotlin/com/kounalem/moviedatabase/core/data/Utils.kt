package com.kounalem.moviedatabase.core.data

import com.kounalem.moviedatabase.network.NetworkResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf

private fun <T, R> mapNetworkResponseToOutcome(response: NetworkResponse<T>, mapper: (T) -> R): Outcome<R> {
    return when (response) {
        is NetworkResponse.Success -> Outcome.Success(mapper(response.body))
        is NetworkResponse.Error -> Outcome.Error(response.message)
        is NetworkResponse.Exception -> Outcome.Exception(response.exception)
    }
}

internal fun <T, R> Flow<NetworkResponse<T>>.mapToOutcome(mapper: (T) -> R): Flow<Outcome<R>> {
    return this.flatMapLatest { response ->
        flowOf(mapNetworkResponseToOutcome(response, mapper))
    }.catch { e ->
        emit(Outcome.Exception(e))
    }
}