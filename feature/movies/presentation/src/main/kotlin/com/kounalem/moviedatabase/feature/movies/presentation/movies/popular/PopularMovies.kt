package com.kounalem.moviedatabase.feature.movies.presentation.movies.popular

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.kounalem.moviedatanase.core.ui.components.MovieOutlinedTextField
import com.kounalem.moviedatanase.core.ui.components.PaginationList
import com.kounalem.moviedatanase.core.ui.model.ListItemModel
import com.kounalem.moviedatanase.core.ui.small
import com.kounalem.moviedatanase.core.ui.xsmall

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
internal fun PopularMoviesView(
    state: PopularMoviesContract.State,
    navigateToDetails: (Int) -> Unit,
    event: (PopularMoviesContract.Event) -> Unit,
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
    event: (PopularMoviesContract.Event) -> Unit,
    loadNextItems: () -> Unit,
) {
    val swipeRefreshState = rememberSwipeRefreshState(
        isRefreshing = (state as? PopularMoviesContract.State.Info)?.isRefreshing ?: false
    )
    val listState = rememberLazyListState()

    MovieOutlinedTextField(
        modifier = Modifier
            .fillMaxWidth(),
        searchQuery = state.searchQuery,
        event = {
            event(PopularMoviesContract.Event.OnSearchQueryChange(it))
        }
    )

    PaginationList(
        modifier = Modifier,
        refreshState = swipeRefreshState,
        listState = listState,
        isFetchingNewMovies = state.fetchingNewMovies,
        items = state.movies.map { item ->
            ListItemModel(
                id = item.id,
                imagePath = item.posterPath,
                description = item.overview,
                title = item.title,
            )
        },
        searchQuery = state.searchQuery,
        endReached = state.endReached,
        refreshEvent = { event(PopularMoviesContract.Event.Refresh) },
        loadNextItems = { loadNextItems() },
        selected = { id -> navigateToDetails(id) }
    )
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
                PopularMoviesContract.State.Info.Movie(
                    id = 0,
                    posterPath = "",
                    title = "title1",
                    overview = "overview",
                ),
                PopularMoviesContract.State.Info.Movie(
                    id = 0,
                    posterPath = "",
                    title = "title1",
                    overview = "overview",
                ),
                PopularMoviesContract.State.Info.Movie(
                    id = 0,
                    posterPath = "",
                    title = "title1",
                    overview = "overview",
                ),
                PopularMoviesContract.State.Info.Movie(
                    id = 0,
                    posterPath = "",
                    title = "title1",
                    overview = "overview",
                ),
                PopularMoviesContract.State.Info.Movie(
                    id = 0,
                    posterPath = "",
                    title = "title1",
                    overview = "overview",
                ),
                PopularMoviesContract.State.Info.Movie(
                    id = 0,
                    posterPath = "",
                    title = "title1",
                    overview = "overview",
                ),
                PopularMoviesContract.State.Info.Movie(
                    id = 0,
                    posterPath = "",
                    title = "title1",
                    overview = "overview",
                ),
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