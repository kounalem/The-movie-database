package com.kounalem.moviedatabase.feature.movies.presentation.movies.details

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kounalem.moviedatabase.core.ui.theming.HorizontalSpace
import com.kounalem.moviedatabase.core.ui.theming.PreviewBox
import com.kounalem.moviedatabase.core.ui.R
import com.kounalem.moviedatabase.core.ui.components.MovieText
import com.kounalem.moviedatabase.core.ui.components.MovieTopAppBar
import com.kounalem.moviedatabase.core.ui.theming.large
import com.kounalem.moviedatabase.core.ui.theming.small
import com.kounalem.moviedatabase.core.ui.theming.xsmall
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun MovieDetails(popBackStack: (favourite: Boolean) -> Unit) {
    val viewModel = hiltViewModel<DetailsViewModel>()
    val state = viewModel.uiState.collectAsStateWithLifecycle().value
    val favourite = (state as? DetailsContract.State.Info)?.isFavourite ?: false
    BackHandler(onBack = { popBackStack(favourite) })
    DetailsView(popBackStack, state, onFavouriteClicked = viewModel::onFavouriteClicked)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DetailsView(
    popBackStack: (favourite: Boolean) -> Unit,
    state: DetailsContract.State,
    onFavouriteClicked: () -> Unit,
) {
    val scrollState = rememberScrollState()
    Box {
        (state as? DetailsContract.State.Error)?.let {
            MovieText(
                text = it.value,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.error,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
            )
        }
        (state as? DetailsContract.State.Loading)?.let {
            Row(
                modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(small),
                horizontalArrangement = Arrangement.Center,
            ) {
                CircularProgressIndicator()
            }
        }
        (state as? DetailsContract.State.Info)?.let {
            Column(
                modifier = Modifier.fillMaxSize(),
            ) {
                MovieTopAppBar(
                    text = state.title,
                    popBackStack = { popBackStack(state.isFavourite) }
                )
                Box {
                    if (state.poster?.isNotEmpty() == true) {
                        GlideImage(
                            modifier =
                            Modifier
                                .fillMaxSize(),
                            imageModel = { state.poster },
                            imageOptions =
                            ImageOptions(
                                contentScale = ContentScale.Crop,
                                alignment = Alignment.Center,
                            ),
                            previewPlaceholder = R.drawable.the_room,
                        )
                    }
                    Column(
                        Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.4f))
                            .verticalScroll(scrollState),
                    ) {
                        Box(
                            modifier =
                            Modifier
                                .fillMaxWidth(),
                        ) {
                            CircleButtonWithHeart(
                                modifier = Modifier.align(Alignment.TopEnd),
                                buttonColor = if (state.isFavourite) Color.Red.copy(alpha = 0.6f) else Color.Red,
                                iconTint = if (state.isFavourite) Color.White.copy(alpha = 0.6f) else Color.White,
                                onClick = onFavouriteClicked,
                            )
                        }

                        MovieText(
                            modifier = Modifier.padding(start = large),
                            text = state.rate,
                            color = Color.White,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1,
                        )

                        HorizontalSpace(xsmall)
                        MovieText(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(large),
                            overflow = TextOverflow.Ellipsis,
                            text = state.overview,
                            color = Color.White,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CircleButtonWithHeart(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    buttonColor: Color = Color.Red,
    icon: ImageVector = Icons.Filled.Favorite,
    iconTint: Color = Color.White,
) {
    Button(
        onClick = onClick,
        modifier = modifier.padding(small),
        shape = androidx.compose.foundation.shape.CircleShape,
        colors = ButtonDefaults.buttonColors(buttonColor),
    ) {
        Icon(
            imageVector = icon,
            contentDescription = "favourite movie",
            tint = iconTint,
        )
    }
}

@Preview(backgroundColor = 0xffffffff, showBackground = true)
@Composable
fun MovieDetailsScreenPreview() {
    PreviewBox {
        DetailsView(
            state =
            DetailsContract.State.Info(
                title = "title",
                overview = "overview",
                rate = "rate'",
                poster = "poster",
                isFavourite = true,
            ),
            onFavouriteClicked = {},
            popBackStack = {},
        )
    }
}

@Preview(backgroundColor = 0xffffffff, showBackground = true)
@Composable
fun MovieDetailsScreenLoadingPreview() {
    DetailsView(
        state = DetailsContract.State.Loading,
        onFavouriteClicked = {},
        popBackStack = {},
    )
}

@Preview(backgroundColor = 0xffffffff, showBackground = true)
@Composable
fun MovieDetailsScreenErrorPreview() {
    DetailsView(
        popBackStack = {},
        state = DetailsContract.State.Error("error"),
        onFavouriteClicked = {},
    )
}
