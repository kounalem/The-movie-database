package com.kounalem.moviedatabase.util.paginator

import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory

@AssistedFactory
interface PaginatorFactory<Key> {
    fun create(
        @Assisted initialKey: Key,
        @Assisted onRequest: suspend (nextKey: Key) -> Unit,
        @Assisted getNextKey: suspend (currentKey: Key) -> Key,
    ): Paginator<Key>
}