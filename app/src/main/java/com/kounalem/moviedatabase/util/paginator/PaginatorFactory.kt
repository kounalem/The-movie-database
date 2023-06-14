package com.kounalem.moviedatabase.util.paginator

import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import kotlinx.coroutines.flow.Flow

@AssistedFactory
interface PaginatorFactory<Key, Item> {
    fun create(
        @Assisted initialKey: Key,
        @Assisted onRequest: suspend (nextKey: Key) -> Flow<Item>,
        @Assisted getNextKey: suspend (Item) -> Key,
        @Assisted onUpdate: suspend (items: Item, newKey: Key) -> Unit,
        @Assisted onLoadUpdated: () -> Unit,
    ): Paginator<Key, Item>

}