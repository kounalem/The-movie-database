package com.kounalem.moviedatanase.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun PreviewBox(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    MovieDatabaseTheme {
        Box(
            modifier = modifier.background(MaterialTheme.colorScheme.background),
        ) {
            content()
        }
    }
}
