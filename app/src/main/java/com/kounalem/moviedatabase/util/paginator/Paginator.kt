package com.kounalem.moviedatabase.util.paginator

import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest


class Paginator<Key, Item> @AssistedInject constructor(
    @Assisted private val initialKey: Key,
    @Assisted private val onRequest: suspend (nextKey: Key) -> Flow<Item>,
    @Assisted private val getNextKey: suspend (Item) -> Key,
    @Assisted private inline val onUpdate: suspend (items: Item, newKey: Key) -> Unit,
    @Assisted private inline val onLoadUpdated: () -> Unit,
) {
    private var currentKey = initialKey
    private var isMakingRequest = false

    suspend fun loadNextItems() {
        if (isMakingRequest) {
            return
        }
        isMakingRequest = true
        onLoadUpdated()
        val result: Flow<Item> = onRequest(currentKey)
        isMakingRequest = false
        result.collectLatest {
            currentKey = getNextKey(it)
            onUpdate(it, currentKey)
        }
    }

    fun reset() {
        currentKey = initialKey
    }
}