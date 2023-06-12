package com.kounalem.moviedatabaase.presentation.popular

import com.kounalem.moviedatabaase.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest

internal class Paginator<Key, Item>(
    private val initialKey: Key,
    private inline val onRequest: suspend (nextKey: Key) -> Flow<Resource<Item>>,
    private inline val getNextKey: suspend (Resource<Item>) -> Key,
) {

    private var currentKey = initialKey
    private var isMakingRequest = false
    private val mutableFlowOf = MutableSharedFlow<Resource<Pair<Item, Key>>>()

    suspend fun loadNextItems() {
        if (isMakingRequest) {
            return
        }
        isMakingRequest = true
        mutableFlowOf.emit(Resource.Loading())
        val result: Flow<Resource<Item>> = onRequest(currentKey)
        isMakingRequest = false
        result.collectLatest {
            if (it.message != null) {
                mutableFlowOf.emit(Resource.Loading())
                mutableFlowOf.emit(Resource.Error("Data could not be retrieved."))
            } else if (it.data != null) {
                currentKey = getNextKey(it)
                mutableFlowOf.emit(Resource.Loading())
                mutableFlowOf.emit(Resource.Success(Pair(it.data, currentKey)))
            }
        }
    }

    fun result(): Flow<Resource<Pair<Item, Key>>> = mutableFlowOf

    fun reset() {
        currentKey = initialKey
    }
}