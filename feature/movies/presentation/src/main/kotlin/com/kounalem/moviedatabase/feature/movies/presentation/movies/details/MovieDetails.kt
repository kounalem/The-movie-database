package com.kounalem.moviedatabase.feature.movies.presentation.movies.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kounalem.moviedatabase.core.ui.R
import com.kounalem.moviedatabase.feature.movies.presentation.movies.details.DetailsContract
import com.kounalem.moviedatabase.feature.movies.presentation.movies.details.DetailsViewModel
import com.kounalem.moviedatanase.core.ui.HorizontalSpace
import com.kounalem.moviedatanase.core.ui.PreviewBox
import com.kounalem.moviedatanase.core.ui.large
import com.kounalem.moviedatanase.core.ui.small
import com.kounalem.moviedatanase.core.ui.xsmall
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun MovieDetails(
    popBackStack: () -> Unit,
    id: Int,
) {
    val viewModel = hiltViewModel<DetailsViewModel>()
    val state = viewModel.state.collectAsStateWithLifecycle().value
    DetailsView(popBackStack, state, event = viewModel::onEvent)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DetailsView(
    popBackStack: () -> Unit,
    state: DetailsContract.State,
    event: (DetailsContract.MovieDetailsEvent) -> Unit,
) {
    Box {
        (state as? DetailsContract.State.Error)?.let {
            Text(
                text = it.value,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onBackground,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
            )
        }
        (state as? DetailsContract.State.Loading)?.let {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(small),
                horizontalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator()
            }
        }
        (state as? DetailsContract.State.Info)?.let {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                TopAppBar(
                    title = { Text(text = state.title) },
                    navigationIcon = {
                        IconButton(onClick = popBackStack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary,
                        navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                        actionIconContentColor = MaterialTheme.colorScheme.onSecondary
                    ),
                )
                Box {
                    GlideImage(
                        modifier = Modifier
                            .fillMaxSize(),
                        imageModel = { state.poster.orEmpty() },
                        imageOptions = ImageOptions(
                            contentScale = ContentScale.Crop,
                            alignment = Alignment.Center
                        ),
                        previewPlaceholder = R.drawable.the_room,
                    )

                    Column(
                        Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.4f))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            CircleButtonWithHeart(
                                modifier = Modifier.align(Alignment.TopEnd),
                                buttonColor = if (state.isFavourite) Color.Red.copy(alpha = 0.6f) else Color.Red,
                                iconTint = if (state.isFavourite) Color.White.copy(alpha = 0.6f) else Color.White,
                                onClick = { event(DetailsContract.MovieDetailsEvent.FavouriteAction) }
                            )
                        }

                        Text(
                            modifier = Modifier.padding(start = large),
                            text = state.rate,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp,
                            color = Color.White,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1,
                        )

                        HorizontalSpace(xsmall)
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(large),
                            overflow = TextOverflow.Ellipsis,
                            text = state.overview,
                            fontWeight = FontWeight.Light,
                            color = Color.White
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
        colors = ButtonDefaults.buttonColors(buttonColor)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = "favourite movie",
            tint = iconTint
        )
    }
}

@Preview(backgroundColor = 0xffffffff, showBackground = true)
@Composable
fun PopularMoviesScreenPreview() {
    PreviewBox {
        DetailsView(
            state = DetailsContract.State.Info(
                title = "title",
                overview = "overview",
                rate = "rate'",
                poster = null,
                isFavourite = true,
            ),
            event = {},
            popBackStack = {},
        )
    }
}

@Preview(backgroundColor = 0xffffffff, showBackground = true)
@Composable
fun PopularMoviesScreenLoadingPreview() {
    DetailsView(
        state = DetailsContract.State.Loading,
        event = {},
        popBackStack = {},
    )
}

@Preview(backgroundColor = 0xffffffff, showBackground = true)
@Composable
fun PopularMoviesScreenErrorPreview() {
    DetailsView(
        popBackStack = {},
        state = DetailsContract.State.Error("error"),
        event = {},
    )
}