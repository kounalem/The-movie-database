package com.kounalem.moviedatabaase.presentation.popular

import com.kounalem.moviedatabaase.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest

internal class Paginator<Key, Item>(
    private val initialKey: Key,
    private inline val onLoadUpdated: (Boolean) -> Unit,
    private inline val onRequest: suspend (nextKey: Key) -> Flow<Resource<Item>>,
    private inline val getNextKey: suspend (Resource<Item>) -> Key,
    private inline val onError: suspend (Throwable?) -> Unit,
    private inline val onSuccess: suspend (items: Item, newKey: Key) -> Unit
) {

    private var currentKey = initialKey
    private var isMakingRequest = false

    suspend fun loadNextItems() {
        if (isMakingRequest) {
            return
        }
        isMakingRequest = true
        onLoadUpdated(true)
        val result: Flow<Resource<Item>> = onRequest(currentKey)
        isMakingRequest = false
        result.collectLatest {
            if (it.message != null) {
                onError(Throwable(it.message))
                onLoadUpdated(false)
            } else if (it.data != null) {
                currentKey = getNextKey(it)
                onSuccess(it.data, currentKey)
                onLoadUpdated(false)
            }
        }
    }

    fun reset() {
        currentKey = initialKey
    }
}