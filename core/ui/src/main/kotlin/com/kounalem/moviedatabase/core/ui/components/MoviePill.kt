package com.kounalem.moviedatabase.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import com.airbnb.android.showkase.annotation.ShowkaseComposable
import com.kounalem.moviedatabase.core.ui.theming.PreviewBox
import com.kounalem.moviedatabase.core.ui.ShowkaseComposableGroup
import com.kounalem.moviedatabase.core.ui.annotations.ScreenPreview
import com.kounalem.moviedatabase.core.ui.theming.large
import com.kounalem.moviedatabase.core.ui.theming.light_onBackground
import com.kounalem.moviedatabase.core.ui.theming.medium
import com.kounalem.moviedatabase.core.ui.throttlingListener
import com.kounalem.moviedatabase.core.ui.theming.xsmall

@Composable
fun MoviePill(
    text: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = light_onBackground,
    iconTextSpacing: Dp = medium,
    onClick: (() -> Unit)? = null,
) {
    Row(
        modifier =
            modifier
                .background(
                    color = backgroundColor,
                    shape = RoundedCornerShape(50),
                )
                .clip(RoundedCornerShape(50))
                .clickable(
                    enabled = onClick != null,
                    onClick = throttlingListener(onClick = { onClick?.invoke() }),
                )
                .padding(PaddingValues(start = large, end = large, top = xsmall, bottom = xsmall)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(iconTextSpacing),
    ) {
        MovieText(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            color = Color.White,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            modifier =
                Modifier
                    .weight(1f, fill = false),
        )
    }
}

@Composable
@ScreenPreview
private fun PillLocalPreview() {
    MoviePill(
        text = "Hello",
    )
}

@Composable
@ShowkaseComposable(name = "Pill", group = ShowkaseComposableGroup.COMPONENTS)
fun PillPreview() {
    PreviewBox {
        MoviePill(
            text = "Hello",
        )
    }
}
