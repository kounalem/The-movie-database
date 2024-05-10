package com.kounalem.moviedatabase.core.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.airbnb.android.showkase.annotation.ShowkaseComposable
import com.kounalem.moviedatabase.core.ui.theming.PreviewBox
import com.kounalem.moviedatabase.core.ui.ShowkaseComposableGroup
import com.kounalem.moviedatabase.core.ui.annotations.ScreenPreview

@Composable
fun MovieOutlinedTextField(
    modifier: Modifier,
    searchQuery: String?,
    event: (String) -> Unit,
) {
    OutlinedTextField(
        textStyle = MaterialTheme.typography.bodyMedium,
        value = searchQuery.orEmpty(),
        onValueChange = {
            event(it)
        },
        modifier = modifier,
        placeholder = {
            MovieText(text = "Search...", style = MaterialTheme.typography.bodyMedium)
        },
        maxLines = 1,
        singleLine = true,
        trailingIcon = {
            if (searchQuery?.isNotEmpty() == true) {
                Icon(
                    imageVector = Icons.Filled.Clear,
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier =
                    Modifier.clickable {
                        event("")
                    },
                )
            }
        },
    )
}

@Composable
@ScreenPreview
private fun MovieOutlinedTextFieldLocalPreview() {
    MovieOutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        searchQuery = "",
        event = {},
    )
}

@Composable
@ScreenPreview
private fun MovieOutlinedTextWithQueryFieldLocalPreview() {
    MovieOutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        searchQuery = "query",
        event = {},
    )
}

@Composable
@ShowkaseComposable(name = "OutLinedTextField", group = ShowkaseComposableGroup.COMPONENTS)
fun MovieOutlinedTextFieldPreview() {
    PreviewBox {
        MovieOutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            searchQuery = "",
            event = {},
        )
    }
}
