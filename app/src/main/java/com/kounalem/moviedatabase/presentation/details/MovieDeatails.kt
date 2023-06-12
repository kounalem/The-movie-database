package com.kounalem.moviedatabase.presentation.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.hilt.navigation.compose.hiltViewModel
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage

data class MovieDetailsScreenArgs(
    val title: String,
    val overview: String,
    val rate: Double,
    val id: Int,
)

@Composable
@RootNavGraph
@Destination(navArgsDelegate = MovieDetailsScreenArgs::class)
fun MovieDetails() {
    val viewModel = hiltViewModel<DetailsViewModel>()
    val state = viewModel.state.collectAsStateWithLifecycle().value
    DetailsView(state, event = viewModel::onEvent)
}

@Composable
private fun DetailsView(
    state: DetailsContract.State,
    event: (DetailsContract.MovieDetailsEvent) -> Unit,
) {
    Box {
        if (state.errorText?.isNotEmpty() == true) {
            Text(
                text = state.errorText,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onBackground,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
            )

        } else if (state.isLoading) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {

                Box {
                    state.poster?.let {
                        GlideImage(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            imageModel = { state.poster },
                            imageOptions = ImageOptions(
                                contentScale = ContentScale.Crop,
                                alignment = Alignment.Center
                            )
                        )
                    }
                    CircleButtonWithHeart(
                        modifier = Modifier.align(Alignment.TopEnd),
                        buttonColor = if (state.isFavourite) Color.Red.copy(alpha = 0.6f) else Color.Red,
                        iconTint = if (state.isFavourite) Color.White.copy(alpha = 0.6f) else Color.White,
                        onClick = { event(DetailsContract.MovieDetailsEvent.FavouriteAction) }
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = state.title,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onBackground,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                    )

                    Text(
                        text = state.rate,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onBackground,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                    )
                }

                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    overflow = TextOverflow.Ellipsis,
                    text = state.overview,
                    fontWeight = FontWeight.Light,
                    color = MaterialTheme.colorScheme.onBackground
                )

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
        modifier = modifier.padding(8.dp),
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
    DetailsView(
        state = DetailsContract.State(
            errorText = null,
            title = "title",
            overview = "overview",
            rate = "rate'",
            poster = null,
            isFavourite = true,
            isLoading = false,
        ),
        event = {},
    )
}

@Preview(backgroundColor = 0xffffffff, showBackground = true)
@Composable
fun PopularMoviesScreenLoadingPreview() {
    DetailsView(
        state = DetailsContract.State(
            errorText = null,
            title = "title",
            overview = "overview",
            rate = "rate'",
            poster = null,
            isFavourite = true,
            isLoading = true,
        ),
        event = {},
    )
}

@Preview(backgroundColor = 0xffffffff, showBackground = true)
@Composable
fun PopularMoviesScreenErrorPreview() {
    DetailsView(
        state = DetailsContract.State(
            errorText = "error",
            title = "title",
            overview = "overview",
            rate = "rate'",
            poster = null,
            isFavourite = true,
            isLoading = false,
        ),
        event = {},
    )
}