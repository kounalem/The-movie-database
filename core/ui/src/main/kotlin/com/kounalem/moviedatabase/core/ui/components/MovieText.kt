package com.kounalem.moviedatabase.core.ui.components

import androidx.annotation.StringRes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.airbnb.android.showkase.annotation.ShowkaseComposable
import com.kounalem.moviedatabase.core.ui.theming.PreviewBox
import com.kounalem.moviedatabase.core.ui.ShowkaseComposableGroup
import com.kounalem.moviedatabase.core.ui.annotations.ScreenPreview

@Composable
fun MovieText(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.bodyMedium,
    textAlign: TextAlign = TextAlign.Start,
    overflow: TextOverflow = TextOverflow.Clip,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    color: Color = Color.Unspecified,
) {
    Text(
        style = style,
        modifier = modifier,
        text = text,
        textAlign = textAlign,
        overflow = overflow,
        maxLines = maxLines,
        minLines = minLines,
        color = color,
    )
}

@Composable
fun MovieText(
    @StringRes text: Int,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.bodyMedium,
    textAlign: TextAlign = TextAlign.Start,
    overflow: TextOverflow = TextOverflow.Clip,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    color: Color = Color.Unspecified,
) {
    Text(
        style = style,
        modifier = modifier,
        text = stringResource(text),
        textAlign = textAlign,
        overflow = overflow,
        maxLines = maxLines,
        minLines = minLines,
        color = color,
    )
}

@Composable
@ScreenPreview
private fun TextLocalPreview() {
    MovieText(
        text = "Hello",
    )
}

@Composable
@ShowkaseComposable(name = "Text", group = ShowkaseComposableGroup.COMPONENTS)
fun PTextPreview() {
    PreviewBox {
        MovieText(
            text = "Hello",
        )
    }
}
