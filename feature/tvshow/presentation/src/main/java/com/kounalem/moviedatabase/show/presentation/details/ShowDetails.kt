package com.kounalem.moviedatabase.show.presentation.details

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kounalem.moviedatabase.core.ui.theming.HorizontalSpace
import com.kounalem.moviedatabase.core.ui.theming.PreviewBox
import com.kounalem.moviedatabase.core.ui.R
import com.kounalem.moviedatabase.core.ui.components.MoviePill
import com.kounalem.moviedatabase.core.ui.components.MovieText
import com.kounalem.moviedatabase.core.ui.components.MovieTopAppBar
import com.kounalem.moviedatabase.core.ui.theming.large
import com.kounalem.moviedatabase.core.ui.theming.small
import com.kounalem.moviedatabase.core.ui.theming.xlarge
import com.kounalem.moviedatabase.core.ui.theming.xsmall
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun ShowDetails(popBackStack: () -> Unit) {
    val viewModel = hiltViewModel<DetailsViewModel>()
    val state = viewModel.uiState.collectAsStateWithLifecycle().value
    BackHandler(onBack = popBackStack)
    DetailsView(popBackStack, state, onFavouriteClicked = viewModel::onFavouriteClicked)
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
internal fun DetailsView(
    popBackStack: () -> Unit,
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
                    popBackStack = popBackStack
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
                            state.firstAirDate?.let { firstAirDate ->
                                MovieText(
                                    modifier =
                                    Modifier
                                        .padding(start = large, top = xlarge)
                                        .align(Alignment.TopStart),
                                    text = "$firstAirDate - ${state.lastAirDate}",
                                    color = Color.White,
                                    style = MaterialTheme.typography.bodyLarge,
                                    overflow = TextOverflow.Ellipsis,
                                    maxLines = 1,
                                )
                            }
                            CircleButtonWithHeart(
                                modifier = Modifier.align(Alignment.TopEnd),
                                buttonColor = if (state.isFavourite) Color.Red.copy(alpha = 0.6f) else Color.Red,
                                iconTint = if (state.isFavourite) Color.White.copy(alpha = 0.6f) else Color.White,
                                onClick = onFavouriteClicked,
                            )
                        }

                        state.type?.let { type ->
                            MovieText(
                                modifier = Modifier.padding(start = large),
                                text = type,
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color.White,
                                overflow = TextOverflow.Ellipsis,
                                maxLines = 1,
                            )
                        }

                        HorizontalSpace(xsmall)
                        MovieText(
                            modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(large),
                            overflow = TextOverflow.Ellipsis,
                            text = state.overview,
                            color = Color.White,
                            style = MaterialTheme.typography.bodyMedium,
                        )
                        FlowRow(modifier = Modifier.padding(small)) {
                            state.languages?.forEach {
                                MoviePill(text = it)
                            }
                        }
                        state.seasons?.filter { it.posterPath?.isNotEmpty() ?: false }
                            ?.let { season ->
                                LazyRow {
                                    items(season,  key = { it.name }) {item ->
                                        Card(
                                            modifier =
                                            Modifier
                                                .width(150.dp)
                                                .padding(horizontal = xsmall),
                                        ) {
                                            Box {
                                                if (item.posterPath?.isNotEmpty() == true) {
                                                    GlideImage(
                                                        imageModel = { item.posterPath },
                                                        imageOptions =
                                                        ImageOptions(
                                                            contentScale = ContentScale.Crop,
                                                            alignment = Alignment.Center,
                                                        ),
                                                        previewPlaceholder = R.drawable.the_room,
                                                    )
                                                }
                                                MovieText(
                                                    modifier =
                                                    Modifier
                                                        .fillMaxWidth()
                                                        .padding(large)
                                                        .align(Alignment.BottomCenter),
                                                    overflow = TextOverflow.Ellipsis,
                                                    text = state.overview,
                                                    maxLines = 3,
                                                    style = MaterialTheme.typography.labelMedium,
                                                    color = Color.White,
                                                )
                                            }
                                        }
                                    }
                                }
                            }
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
            contentDescription = "favourite show",
            tint = iconTint,
        )
    }
}

@Preview(backgroundColor = 0xffffffff, showBackground = true)
@Composable
fun showDetailsScreenPreview() {
    PreviewBox {
        DetailsView(
            state =
            DetailsContract.State.Info(
                title = "title",
                overview = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
                poster = "poster",
                isFavourite = true,
                seasons =
                listOf(
                    DetailsContract.State.Info.Season(
                        airDate = "aeque",
                        episodeCount = 5438,
                        id = 1121,
                        name = "Marcy Rivera",
                        overview = "definitiones",
                        posterPath = "posterPath",
                        seasonNumber = 6885,
                        voteAverage = 1912,
                    ),
                    DetailsContract.State.Info.Season(
                        airDate = "vehicula",
                        episodeCount = 7174,
                        id = 6749,
                        name = "Earnestine Campos",
                        overview = "ornatus",
                        posterPath = "posterPath",
                        seasonNumber = 6168,
                        voteAverage = 1389,
                    ),
                    DetailsContract.State.Info.Season(
                        airDate = "saepe",
                        episodeCount = 6421,
                        id = 9141,
                        name = "Marci Craig",
                        overview = "legimus",
                        posterPath = "posterPath",
                        seasonNumber = 7029,
                        voteAverage = 8033,
                    ),
                    DetailsContract.State.Info.Season(
                        airDate = "mauris",
                        episodeCount = 2761,
                        id = 8413,
                        name = "Terry Olsen",
                        overview = "decore",
                        posterPath = "posterPath",
                        seasonNumber = 2025,
                        voteAverage = 3260,
                    ),
                ),
                languages = listOf("English", "Greek"),
                lastAirDate = "ON GOING",
                type = null,
                firstAirDate = "21-2-22",
            ),
            onFavouriteClicked = {},
            popBackStack = {},
        )
    }
}

@Preview(backgroundColor = 0xffffffff, showBackground = true)
@Composable
fun ShowDetailsScreenLoadingPreview() {
    DetailsView(
        state = DetailsContract.State.Loading,
        onFavouriteClicked = {},
        popBackStack = {},
    )
}

@Preview(backgroundColor = 0xffffffff, showBackground = true)
@Composable
fun ShowDetailsScreenErrorPreview() {
    DetailsView(
        popBackStack = {},
        state = DetailsContract.State.Error("error"),
        onFavouriteClicked = {},
    )
}
