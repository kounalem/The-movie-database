package com.kounalem.moviedatabase.core.data.utils

import app.cash.turbine.ReceiveTurbine

fun <T> ReceiveTurbine<T>.onLatestItem(onItem:(T) -> Unit): ReceiveTurbine<T> {
    onItem(expectMostRecentItem())
    return this
}

