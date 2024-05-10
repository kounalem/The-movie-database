package com.kounalem.moviedatabase.show.presentation.popular

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.kounalem.moviedatabase.core.ui.components.MovieOutlinedTextField
import com.kounalem.moviedatabase.core.ui.components.MoviePaginationList
import com.kounalem.moviedatabase.core.ui.components.MovieText
import com.kounalem.moviedatabase.core.ui.model.ListItemModel
import com.kounalem.moviedatabase.core.ui.theming.small
import com.kounalem.moviedatabase.core.ui.theming.xsmall

@Composable
fun PopularShowScreen(navigateToTvShow: (Int) -> Unit) {
    val viewModel = hiltViewModel<PopularShowsViewModel>()
    val state = viewModel.uiState.collectAsStateWithLifecycle().value

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is PopularShowsContract.Event.NavigateToDetails -> navigateToTvShow(event.id)
            }
        }
    }

    PopularShowsView(
        state = state,
        navigateToTvShow = viewModel::navigateToDetails,
        search = viewModel::onSearchQueryChange,
        loadNextItems = viewModel::loadNextItems,
        refresh = viewModel::refreshElements,
    )
}

@Composable
internal fun PopularShowsView(
    state: PopularShowsContract.State,
    navigateToTvShow: (Int) -> Unit,
    search: (query: String) -> Unit,
    loadNextItems: () -> Unit,
    refresh: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        when (state) {
            is PopularShowsContract.State.Error -> {
                MovieText(
                    modifier = Modifier.padding(vertical = small, horizontal = xsmall),
                    text = "Oooopsie",
                    color = MaterialTheme.colorScheme.error,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                )
            }

            is PopularShowsContract.State.Loading -> {
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

            is PopularShowsContract.State.Info -> {
                ShowList(
                    state = state,
                    navigateToTvShow = navigateToTvShow,
                    search = search,
                    loadNextItems = loadNextItems,
                    refresh = refresh,
                )
            }
        }
    }
}

@Composable
private fun ShowList(
    state: PopularShowsContract.State.Info,
    navigateToTvShow: (Int) -> Unit,
    search: (query: String) -> Unit,
    refresh: () -> Unit,
    loadNextItems: () -> Unit,
) {
    val swipeRefreshState =
        rememberSwipeRefreshState(
            isRefreshing = (state as? PopularShowsContract.State.Info)?.isRefreshing ?: false,
        )
    val listState = rememberLazyListState()
    MovieOutlinedTextField(
        modifier =
            Modifier
                .fillMaxWidth(),
        searchQuery = state.searchQuery,
        event = search,
    )

    MoviePaginationList(
        modifier = Modifier,
        refreshState = swipeRefreshState,
        listState = listState,
        isFetchingNewMovies = state.fetchingNewShows,
        items =
            state.shows.map { item ->
                ListItemModel(
                    id = item.id,
                    imagePath = item.posterPath,
                    description = item.overview,
                    title = item.title,
                )
            },
        searchQuery = state.searchQuery,
        endReached = state.endReached,
        refreshEvent = refresh,
        loadNextItems = { loadNextItems() },
        selected = { id -> navigateToTvShow(id) },
    )
}

@Preview(backgroundColor = 0xffffffff, showBackground = false)
@Composable
fun PopularShowScreenPreview() {
    PopularShowsView(
        state =
            PopularShowsContract.State.Info(
                listOf(
                    PopularShowsContract.State.Info.Show(
                        id = 0,
                        posterPath = "",
                        title = "title1",
                        overview = "overview",
                    ),
                    PopularShowsContract.State.Info.Show(
                        id = 0,
                        posterPath = "",
                        title = "title1",
                        overview = "overview",
                    ),
                    PopularShowsContract.State.Info.Show(
                        id = 0,
                        posterPath = "",
                        title = "title1",
                        overview = "overview",
                    ),
                    PopularShowsContract.State.Info.Show(
                        id = 0,
                        posterPath = "",
                        title = "title1",
                        overview = "overview",
                    ),
                    PopularShowsContract.State.Info.Show(
                        id = 0,
                        posterPath = "",
                        title = "title1",
                        overview = "overview",
                    ),
                    PopularShowsContract.State.Info.Show(
                        id = 0,
                        posterPath = "",
                        title = "title1",
                        overview = "overview",
                    ),
                    PopularShowsContract.State.Info.Show(
                        id = 0,
                        posterPath = "",
                        title = "title1",
                        overview = "overview",
                    ),
                    PopularShowsContract.State.Info.Show(
                        id = 0,
                        posterPath = "",
                        title = "title1",
                        overview = "overview",
                    ),
                    PopularShowsContract.State.Info.Show(
                        id = 0,
                        posterPath = "",
                        title = "title1",
                        overview = "overview",
                    ),
                ),
                searchQuery = "",
                isRefreshing = false,
                endReached = false,
                fetchingNewShows = false,
            ),
        navigateToTvShow = {},
        search = {},
        refresh = {},
        loadNextItems = {},
    )
}
