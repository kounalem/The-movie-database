package com.kounalem.moviedatabase.util.paginator

import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch


class Paginator<Key> @AssistedInject constructor(
    @Assisted private val initialKey: Key,
    @Assisted private val onRequest: suspend (nextKey: Key) -> Unit,
    @Assisted private val getNextKey: suspend (currentKey: Key) -> Key,
) {
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private var currentKey = initialKey
    private var isMakingRequest = false

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