package com.kounalem.moviedatanase.core.ui

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Preview(name = "Light preview", showBackground = true)
@Preview(name = "Dark preview", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
annotation class ScreenPreview

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
