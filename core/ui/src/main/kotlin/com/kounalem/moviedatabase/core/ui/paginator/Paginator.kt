package com.kounalem.moviedatabase.core.ui.paginator

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class Paginator<Key>(
    private val initialKey: Key,
    private val onRequest: suspend (nextKey: Key) -> Unit,
    private val getNextKey: suspend (currentKey: Key) -> Key,
) {
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private var currentKey = initialKey
    private var isMakingRequest = false

    init {
        scope.launch {
            onRequest(currentKey)
            currentKey = getNextKey(currentKey)
        }
    }

    suspend fun loadNextItems() {
        if (isMakingRequest) {
            return
        }
        isMakingRequest = true
        scope.launch {
            onRequest(currentKey)
            currentKey = getNextKey(currentKey)
        }
        isMakingRequest = false
    }

    fun reset() {
        currentKey = initialKey
    }
}
