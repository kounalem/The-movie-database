package com.kounalem.moviedatabase.core.ui.theming

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.kounalem.moviedatabase.core.ui.theming.MovieDatabaseTheme

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
