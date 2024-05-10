package com.kounalem.moviedatabase.core.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.airbnb.android.showkase.annotation.ShowkaseComposable
import com.kounalem.moviedatabase.core.ui.theming.PreviewBox
import com.kounalem.moviedatabase.core.ui.R
import com.kounalem.moviedatabase.core.ui.ShowkaseComposableGroup
import com.kounalem.moviedatabase.core.ui.theming.VerticalSpace
import com.kounalem.moviedatabase.core.ui.annotations.ScreenPreview
import com.kounalem.moviedatabase.core.ui.model.ListItemModel
import com.kounalem.moviedatabase.core.ui.theming.small
import com.kounalem.moviedatabase.core.ui.throttlingListener
import com.kounalem.moviedatabase.core.ui.theming.xsmall
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun MovieListItem(
    model: ListItemModel,
    modifier: Modifier = Modifier,
    selected: (Int) -> Unit,
) {
    Card(
        modifier =
        Modifier
            .padding(vertical = small, horizontal = xsmall)
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = throttlingListener(onClick = { selected(model.id) })),
    ) {
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (model.imagePath.isNotEmpty()) {
                GlideImage(
                    modifier = Modifier.size(100.dp),
                    imageModel = { model.imagePath },
                    loading = {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .align(Alignment.Center),
                        )
                    },
                    imageOptions =
                    ImageOptions(
                        contentScale = ContentScale.Crop,
                        alignment = Alignment.Center,
                    ),
                    previewPlaceholder = R.drawable.the_room,
                )
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = small),
                verticalArrangement = Arrangement.Center,
            ) {
                MovieText(
                    style = MaterialTheme.typography.titleMedium,
                    text = model.title,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Start,
                )

                MovieText(
                    overflow = TextOverflow.Ellipsis,
                    text = model.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    maxLines = 3,
                    textAlign = TextAlign.Start,
                )
            }
        }
    }
}


@Composable
fun MovieListSingleChoiceToggleItem(
    title: String,
    description: String?,
    modifier: Modifier = Modifier,
    toggleInitValue: Boolean,
    onToggle: (Boolean) -> Unit,
) {
    var checkedState = toggleInitValue
    ListToggleItem(
        title = title,
        description = description,
        modifier = modifier,
        checkedState = checkedState,
        onToggle = {
            checkedState = it
            onToggle(it)
        }
    )
}

@Composable
fun MovieListToggleItem(
    title: String,
    description: String?,
    modifier: Modifier = Modifier,
    toggleInitValue: Boolean,
    onToggle: (Boolean) -> Unit,
) {
    val checkedState = remember { mutableStateOf(toggleInitValue) }
    ListToggleItem(
        title = title,
        description = description,
        modifier = modifier,
        checkedState = checkedState.value,
        onToggle = {
            checkedState.value = it
            onToggle(it)
        }
    )
}


@Composable
private fun ListToggleItem(
    title: String,
    description: String?,
    modifier: Modifier = Modifier,
    checkedState: Boolean,
    onToggle: (Boolean) -> Unit,
) {

    Card(
        modifier =
        Modifier
            .padding(vertical = small, horizontal = xsmall)
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = throttlingListener(onClick = { onToggle(checkedState) })),
    ) {
        Row(
            modifier = modifier.padding(start = xsmall),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = small, vertical = small),
                verticalArrangement = Arrangement.Center,
            ) {
                MovieText(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    textAlign = TextAlign.Start,
                )
                description?.let {
                    VerticalSpace(height = xsmall)
                    MovieText(
                        overflow = TextOverflow.Ellipsis,
                        text = description,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 3,
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Start,
                    )
                }
            }
            Switch(
                modifier = Modifier
                    .padding(end = small),
                checked = checkedState,
                onCheckedChange = {
                    onToggle(it)
                },
                thumbContent = if (checkedState) {
                    {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = null,
                            modifier = Modifier.size(SwitchDefaults.IconSize),
                        )
                    }
                } else {
                    null
                }
            )
        }
    }
}

@Composable
@ScreenPreview
private fun ListItemLocalPreview() {
    PreviewBox {
        MovieListItem(
            ListItemModel(
                id = 0,
                imagePath = "path",
                title = "The room",
                description = "Oh hi, Mark. Everybody Betray Me! I Fed Up With This World! You Are Tearing Me Apart, Lisa!",
            ),
            selected = {},
        )
    }
}

@Composable
@ShowkaseComposable(name = "ListItem", group = ShowkaseComposableGroup.ROWS)
fun ListItemPreview() {
    PreviewBox {
        MovieListItem(
            ListItemModel(
                id = 0,
                imagePath = "",
                title = "The room",
                description = "",
            ),
            selected = {},
        )
    }
}

@Composable
@ShowkaseComposable(name = "ListItem", group = ShowkaseComposableGroup.ROWS)
fun ListItemWithDescriptionPreview() {
    PreviewBox {
        MovieListItem(
            ListItemModel(
                id = 0,
                imagePath = "",
                title = "The room",
                description = "Oh hi, Mark. Everybody Betray Me! I Fed Up With This World! You Are Tearing Me Apart, Lisa!",
            ),
            selected = {},
        )
    }
}

@Composable
@ScreenPreview
fun ToggleListItemLocalPreview() {
    PreviewBox {
        MovieListToggleItem(
            title = "The room",
            description = null,
            toggleInitValue = true,
            onToggle = {}
        )
    }
}

@Composable
@ScreenPreview
fun ToggleListItemWithDescriptionLocalPreview() {
    PreviewBox {
        MovieListToggleItem(
            title = "The room",
            description = "Oh hi, Mark. Everybody Betray Me! I Fed Up With This World! You Are Tearing Me Apart, Lisa!",
            toggleInitValue = true,
            onToggle = {}
        )
    }
}


@Composable
@ShowkaseComposable(name = "ListToggleItem", group = ShowkaseComposableGroup.ROWS)
fun ToggleListItemPreview() {
    PreviewBox {
        MovieListToggleItem(
            title = "The room",
            description = "Oh hi, Mark. Everybody Betray Me! I Fed Up With This World! You Are Tearing Me Apart, Lisa!",
            toggleInitValue = true,
            onToggle = {}
        )
    }
}