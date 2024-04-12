package com.kounalem.moviedatanase.core.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.android.showkase.annotation.ShowkaseComposable
import com.kounalem.moviedatabase.core.ui.R
import com.kounalem.moviedatanase.core.ui.HorizontalSpace
import com.kounalem.moviedatanase.core.ui.PreviewBox
import com.kounalem.moviedatanase.core.ui.ShowkaseComposableGroup
import com.kounalem.moviedatanase.core.ui.model.ListItemModel
import com.kounalem.moviedatanase.core.ui.small
import com.kounalem.moviedatanase.core.ui.xsmall
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun MovieListItem(
    model: ListItemModel,
    modifier: Modifier = Modifier,
    selected: (Int) -> Unit,
) {
    Card(modifier = Modifier
        .padding(vertical = small, horizontal = xsmall)
        .clip(RoundedCornerShape(16.dp))
        .clickable {
            selected(model.id)
        }) {
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            GlideImage(
                modifier = Modifier.size(100.dp),
                imageModel = { model.imagePath },
                imageOptions = ImageOptions(
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.Center
                ),
                previewPlaceholder = R.drawable.the_room,
            )
            HorizontalSpace(xsmall)
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = model.title,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                )
                HorizontalSpace(xsmall)
                Text(
                    overflow = TextOverflow.Ellipsis,
                    text = model.description,
                    fontWeight = FontWeight.Light,
                    maxLines = 3,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}


@Composable
@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
private fun ListItemLocalPreview() {
    ListItemPreview()
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
                description = "Oh hi, Mark. Everybody Betray Me! I Fed Up With This World! You Are Tearing Me Apart, Lisa!"
            ), selected = {}
        )
    }
}

