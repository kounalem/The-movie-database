package com.kounalem.moviedatanase.core.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember

@Stable
private class ClickThrottler(
    private val key: String? = null,
    private val throttleDurationMs: Int = 400,
) {
    private var internalLastClickTime: Long = 0L

    private var lastClickTime: Long
        get() = if (key.isNullOrBlank()) internalLastClickTime else (lastClickTimePerKey[key] ?: 0L)
        set(value) =
            if (key.isNullOrBlank()) {
                internalLastClickTime = value
            } else {
                lastClickTimePerKey[key] = value
            }

    fun canClick(): Boolean {
        val currentTime = System.currentTimeMillis()
        val canClick = currentTime - lastClickTime > throttleDurationMs

        if (canClick) {
            lastClickTime = currentTime
        }

        return canClick
    }

    companion object {
        val lastClickTimePerKey = mutableMapOf<String, Long>()
    }
}

@Suppress("unused")
@Composable
fun throttlingListener(
    key: String? = null,
    minimumClickDifferenceMs: Int = 400,
    onClick: () -> Unit,
): () -> Unit {
    val clickThrottler =
        remember(key, minimumClickDifferenceMs) {
            ClickThrottler(
                key,
                minimumClickDifferenceMs,
            )
        }
    return { if (clickThrottler.canClick()) onClick() }
}
