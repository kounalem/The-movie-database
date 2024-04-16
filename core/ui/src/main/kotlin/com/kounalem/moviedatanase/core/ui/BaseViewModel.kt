package com.kounalem.moviedatanase.core.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.AbstractFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

interface BaseViewModel<UiModel, Event> {
    val uiModels: Flow<UiModel>
    val events: Flow<Event>
}

class EventFlow<Event> : AbstractFlow<Event>() {

    // this channel queue events if no active subscribers
    private val eventsQueue = Channel<Event>(capacity = Channel.UNLIMITED)

    // this flow drops events if no active subscribers
    private val eventsFlow = MutableSharedFlow<Event>()

    private val resultFlow = merge(eventsQueue.receiveAsFlow(), eventsFlow)

    override suspend fun collectSafely(collector: FlowCollector<Event>) {
        resultFlow.collect(collector)
    }

    suspend fun emit(event: Event) = eventsFlow.emit(event)

    suspend fun queue(event: Event) = eventsQueue.send(event)
}

/**
 * Queue an event using the viewModelScope of the current ViewModel.
 *
 * Queued events are cached if there are no subscribers on the EventFlow, and emitted to the next
 * subscriber.
 */
context(ViewModel)
fun <Event> EventFlow<Event>.queueAsync(event: Event) {
    viewModelScope.launch { queue(event) }
}

/**
 * Emit an event using the viewModelScope of the current ViewModel.
 *
 * Emitted events are dropped if there are no subscribers on the EventFlow.
 */
context(ViewModel)
fun <Event> EventFlow<Event>.emitAsync(event: Event) {
    viewModelScope.launch { emit(event) }
}

abstract class BaseViewModelImpl<UiModel, Event> : ViewModel(), BaseViewModel<UiModel, Event> {
    override val uiModels: Flow<UiModel>
        get() = uiState.mapNotNull { it }
    val uiState = MutableStateFlow<UiModel?>(null)

    override val events = EventFlow<Event>()

    protected fun <T> MutableSharedFlow<T>.emitAsync(item: T) {
        viewModelScope.launch {
            this@emitAsync.emit(item)
        }
    }
}
