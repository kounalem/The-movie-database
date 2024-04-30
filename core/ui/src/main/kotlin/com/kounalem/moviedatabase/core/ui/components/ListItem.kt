package com.kounalem.moviedatabase.core.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.android.showkase.annotation.ShowkaseComposable
import com.kounalem.moviedatabase.core.ui.PreviewBox
import com.kounalem.moviedatabase.core.ui.R
import com.kounalem.moviedatabase.core.ui.ShowkaseComposableGroup
import com.kounalem.moviedatabase.core.ui.VerticalSpace
import com.kounalem.moviedatabase.core.ui.annotations.ScreenPreview
import com.kounalem.moviedatabase.core.ui.model.ListItemModel
import com.kounalem.moviedatabase.core.ui.small
import com.kounalem.moviedatabase.core.ui.throttlingListener
import com.kounalem.moviedatabase.core.ui.xsmall
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
                Text(
                    text = model.title,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                )

                Text(
                    overflow = TextOverflow.Ellipsis,
                    text = model.description,
                    fontWeight = FontWeight.Light,
                    maxLines = 3,
                    color = MaterialTheme.colorScheme.onBackground,
                )
            }
        }
    }
}

@Composable
fun ListToggleItem(
    title: String,
    description: String?,
    modifier: Modifier = Modifier,
    toggleInitValue: Boolean,
    toggled: (Boolean) -> Unit,
) {

    val checkedState = remember { mutableStateOf(toggleInitValue) }

    Card(
        modifier =
        Modifier
            .padding(vertical = small, horizontal = xsmall)
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = throttlingListener(onClick = { toggled(checkedState.value) })),
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
                Text(
                    text = title,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                )
                description?.let {
                    VerticalSpace(height = xsmall)
                    Text(
                        overflow = TextOverflow.Ellipsis,
                        text = description,
                        fontWeight = FontWeight.Light,
                        maxLines = 3,
                        color = MaterialTheme.colorScheme.onBackground,
                    )
                }
            }
            Switch(
                modifier = Modifier
                    .padding(end = small),
                checked = checkedState.value,
                onCheckedChange = {
                    checkedState.value = it
                    toggled(it)
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
        ListToggleItem(
            title = "The room",
            description = null,
            toggleInitValue = true,
            toggled = {}
        )
    }
}

@Composable
@ScreenPreview
fun ToggleListItemWithDescriptionLocalPreview() {
    PreviewBox {
        ListToggleItem(
            title = "The room",
            description = "Oh hi, Mark. Everybody Betray Me! I Fed Up With This World! You Are Tearing Me Apart, Lisa!",
            toggleInitValue = true,
            toggled = {}
        )
    }
}


@Composable
@ShowkaseComposable(name = "ListToggleItem", group = ShowkaseComposableGroup.ROWS)
fun ToggleListItemPreview() {
    PreviewBox {
        ListToggleItem(
            title = "The room",
            description = "Oh hi, Mark. Everybody Betray Me! I Fed Up With This World! You Are Tearing Me Apart, Lisa!",
            toggleInitValue = true,
            toggled = {}
        )
    }
}