package com.kounalem.moviedatabase.core.ui.theming

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Stable
val xsmall = 4.dp

@Stable
val small = 8.dp

@Stable
val medium = 12.dp

@Stable
val large = 16.dp

@Stable
val xlarge = 20.dp

@Stable
val HorizontalSpacing = 24.dp

@Composable
fun VerticalSpace(
    height: Dp,
    modifier: Modifier = Modifier,
) {
    Spacer(modifier = modifier.height(height))
}

@Composable
fun HorizontalSpace(
    width: Dp,
    modifier: Modifier = Modifier,
) {
    Spacer(modifier = modifier.width(width))
}
