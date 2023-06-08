package com.kounalem.moviedatabaase.presentation.popular

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kounalem.moviedatabaase.domain.models.Movie
import com.kounalem.moviedatabaase.presentation.destinations.MovieDetailsDestination
import com.kounalem.moviedatabaase.presentation.details.MovieDetailsScreenArgs
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.spec.Direction
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.flow.collect

@Composable
@RootNavGraph(start = true)
@Destination
fun PopularMoviesScreen(
    navigator: DestinationsNavigator
) {
    val viewModel = hiltViewModel<PopularMoviesViewModel>()
    val state = viewModel.state.collectAsStateWithLifecycle().value
    PopularMoviesView(
        state = state,
        navigate = { navigator.navigate(it) },
        event = viewModel::onEvent,
        loadNextItems = viewModel::loadNextItems,
    )
}

@Composable
private fun PopularMoviesView(
    state: PopularMoviesContract.State,
    navigate: (Direction) -> Unit,
    event: (PopularMoviesContract.MovieListingsEvent) -> Unit,
    loadNextItems: () -> Unit,
) {

    val listState = rememberLazyListState()
    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        OutlinedTextField(
            value = state.searchQuery,
            onValueChange = {
                event(PopularMoviesContract.MovieListingsEvent.OnSearchQueryChange(it))
            },
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            placeholder = {
                Text(text = "Search...")
            },
            maxLines = 1,
            singleLine = true
        )

        if (state.movies.isNotEmpty()) {
            LazyColumn(state = listState) {
                itemsIndexed(state.movies) { index, item ->
                    if (index >= state.movies.size - 1 && !state.endReached && !state.isLoading) {
                        loadNextItems()
                    }
                    MovieItem(item, selected = { movie ->
                        navigate(
                            MovieDetailsDestination(
                                MovieDetailsScreenArgs(
                                    title = movie.title,
                                    overview = movie.overview,
                                    id = movie.id,
                                    rate = movie.voteAverage
                                )
                            )
                        )
                    })
                }
                item {
                    if (state.isLoading) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                    if (state.errorText?.isNotEmpty() == true) {
                        Text(
                            text = "Oooopsie",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onBackground,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1,
                        )
                    }

                }
            }
        }
    }
}


@Composable
private fun MovieItem(
    movie: Movie,
    modifier: Modifier = Modifier,
    selected: (Movie) -> Unit,
) {
    Card(modifier = Modifier
        .padding(vertical = 8.dp, horizontal = 4.dp)
        .clickable {
            selected(movie)
        }) {
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            GlideImage(
                modifier = Modifier.size(100.dp),
                imageModel = { movie.posterPath },
                imageOptions = ImageOptions(
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.Center
                )
            )
            Spacer(modifier = Modifier.width(4.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = movie.title,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    overflow = TextOverflow.Ellipsis,
                    text = movie.overview,
                    fontWeight = FontWeight.Light,
                    maxLines = 3,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}

@Preview(backgroundColor = 0xffffffff, showBackground = false)
@Composable
fun PopularMoviesScreenPreview() {
    PopularMoviesView(
        state = PopularMoviesContract.State(
            isLoading = false,
            page = 0,
            endReached = false,
            errorText = null,
            searchQuery = "",
            movies = listOf(
                Movie(
                    id = 0,
                    posterPath = "",
                    title = "title1",
                    voteAverage = 1.0,
                    overview = "overview",
                )
            ),
        ),
        navigate = {},
        event = {},
        loadNextItems = {},
    )
}