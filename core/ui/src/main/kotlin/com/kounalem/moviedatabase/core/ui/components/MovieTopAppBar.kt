package com.kounalem.moviedatabase.core.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.airbnb.android.showkase.annotation.ShowkaseComposable
import com.kounalem.moviedatabase.core.ui.theming.PreviewBox
import com.kounalem.moviedatabase.core.ui.ShowkaseComposableGroup
import com.kounalem.moviedatabase.core.ui.annotations.ScreenPreview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieTopAppBar(
    text: String,
    modifier: Modifier = Modifier,
    popBackStack: (() -> Unit)? = null,
) {
    TopAppBar(
        modifier = modifier,
        title = {
            MovieText(
                text = text,
                style = MaterialTheme.typography.titleMedium,
            )
        },
        navigationIcon = {
            popBackStack?.let {
                IconButton(onClick = it) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onSecondary,
        ),
    )
}

@Composable
@ScreenPreview
private fun MovieTopAppBarLocalPreview() {
    MovieTopAppBar(
        text = "Hello",
    )
}

@Composable
@ShowkaseComposable(name = "TopAppBar", group = ShowkaseComposableGroup.COMPONENTS)
fun MovieTopAppBarPreview() {
    PreviewBox {
        MovieTopAppBar(
            text = "Hello",
        )
    }
}
