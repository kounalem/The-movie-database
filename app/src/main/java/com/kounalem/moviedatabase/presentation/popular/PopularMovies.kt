package com.kounalem.moviedatabase.presentation.popular

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.kounalem.moviedatabase.presentation.HorizontalSpace
import com.kounalem.moviedatabase.presentation.small
import com.kounalem.moviedatabase.presentation.xsmall
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun PopularMoviesScreen(
    navigateToDetails: (Int) -> Unit,
) {
    val viewModel = hiltViewModel<PopularMoviesViewModel>()
    val state = viewModel.state.collectAsStateWithLifecycle().value
    PopularMoviesView(
        state = state,
        navigateToDetails = navigateToDetails,
        event = viewModel::onEvent,
        loadNextItems = viewModel::loadNextItems,
    )
}

@Composable
private fun PopularMoviesView(
    state: PopularMoviesContract.State,
    navigateToDetails: (Int) -> Unit,
    event: (PopularMoviesContract.MovieListingsEvent) -> Unit,
    loadNextItems: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        when (state) {
            is PopularMoviesContract.State.Error -> {
                Text(
                    modifier = Modifier.padding(vertical = small, horizontal = xsmall),
                    text = "Oooopsie",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                )
            }

            is PopularMoviesContract.State.Loading -> {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(small),
                    horizontalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is PopularMoviesContract.State.Info -> {
                MovieList(
                    state = state,
                    navigateToDetails = navigateToDetails,
                    event = event,
                    loadNextItems = loadNextItems
                )
            }
        }
    }
}

@Composable
private fun MovieList(
    state: PopularMoviesContract.State.Info,
    navigateToDetails: (Int) -> Unit,
    event: (PopularMoviesContract.MovieListingsEvent) -> Unit,
    loadNextItems: () -> Unit,
) {
    val swipeRefreshState = rememberSwipeRefreshState(
        isRefreshing = (state as? PopularMoviesContract.State.Info)?.isRefreshing ?: false
    )
    val listState = rememberLazyListState()
    val topPadding by animateDpAsState(
        targetValue = if (listState.isScrollInProgress) 0.dp else (56.dp),
        animationSpec = tween(durationMillis = 300)
    )
    if (topPadding > 0.dp) {
        OutlinedTextField(
            value = state.searchQuery.orEmpty(),
            onValueChange = {
                event(PopularMoviesContract.MovieListingsEvent.OnSearchQueryChange(it))
            },
            modifier = Modifier
                .height(topPadding)
                .fillMaxWidth(),
            placeholder = {
                Text(text = "Search...")
            },
            maxLines = 1,
            singleLine = true,
            trailingIcon = {
                if (state.searchQuery?.isNotEmpty() == true) {
                    Icon(
                        imageVector = Icons.Filled.Clear,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.clickable {
                            event(
                                PopularMoviesContract.MovieListingsEvent.OnSearchQueryChange(
                                    ""
                                )
                            )
                        }
                    )
                }
            }
        )
    }

    if (state.movies.isNotEmpty()) {
        SwipeRefresh(
            state = swipeRefreshState,
            onRefresh = {
                event(PopularMoviesContract.MovieListingsEvent.Refresh)
            }
        ) {
            LazyColumn(state = listState) {
                itemsIndexed(state.movies) { index, item ->
                    if (index >= state.movies.size - 1 && !state.endReached && state.searchQuery.isNullOrEmpty()) {
                        loadNextItems()
                    }
                    MovieItem(
                        movie = item,
                        selected = { movie ->
                            navigateToDetails(movie.id)
                        }
                    )
                }
                if (state.fetchingNewMovies) {
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(small),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }
            }
        }
    }
}


@Composable
private fun MovieItem(
    movie: PopularMoviesContract.State.Info.Movie,
    modifier: Modifier = Modifier,
    selected: (PopularMoviesContract.State.Info.Movie) -> Unit,
) {
    Card(modifier = Modifier
        .padding(vertical = small, horizontal = xsmall)
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
            HorizontalSpace(xsmall)
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
                HorizontalSpace(xsmall)
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
        state = PopularMoviesContract.State.Info(
            listOf(
                PopularMoviesContract.State.Info.Movie(
                    id = 0,
                    posterPath = "",
                    title = "title1",
                    overview = "overview",
                ),
            ),
            searchQuery = "",
            isRefreshing = false,
            endReached = false,
            fetchingNewMovies = false,
        ),
        navigateToDetails = {},
        event = {},
        loadNextItems = {},
    )
}