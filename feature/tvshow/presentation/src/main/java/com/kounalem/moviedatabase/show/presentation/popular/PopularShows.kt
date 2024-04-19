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
fun PopularShowScreen(
    navigateToTvShow: (Int) -> Unit,
) {
    val viewModel = hiltViewModel<PopularShowsViewModel>()
    val state = viewModel.state.collectAsStateWithLifecycle().value
    PopularShowsView(
        state = state,
        navigateToTvShow = navigateToTvShow,
        event = viewModel::onEvent,
        loadNextItems = viewModel::loadNextItems,
    )
}

@Composable
internal fun PopularShowsView(
    state: PopularShowsContract.State,
    navigateToTvShow: (Int) -> Unit,
    event: (PopularShowsContract.Event) -> Unit,
    loadNextItems: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        when (state) {
            is PopularShowsContract.State.Error -> {
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

            is PopularShowsContract.State.Loading -> {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(small),
                    horizontalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is PopularShowsContract.State.Info -> {
                ShowList(
                    state = state,
                    navigateToTvShow = navigateToTvShow,
                    event = event,
                    loadNextItems = loadNextItems
                )
            }
        }
    }
}

@Composable
private fun ShowList(
    state: PopularShowsContract.State.Info,
    navigateToTvShow: (Int) -> Unit,
    event: (PopularShowsContract.Event) -> Unit,
    loadNextItems: () -> Unit,
) {
    val swipeRefreshState = rememberSwipeRefreshState(
        isRefreshing = (state as? PopularShowsContract.State.Info)?.isRefreshing ?: false
    )
    val listState = rememberLazyListState()
    MovieOutlinedTextField(
        modifier = Modifier
            .fillMaxWidth(),
        searchQuery = state.searchQuery,
        event = {
            event(PopularShowsContract.Event.OnSearchQueryChange(it))
        }
    )

    PaginationList(
        modifier = Modifier,
        refreshState = swipeRefreshState,
        listState = listState,
        isFetchingNewMovies = state.fetchingNewShows,
        items = state.shows.map { item ->
            ListItemModel(
                id = item.id,
                imagePath = item.posterPath,
                description = item.overview,
                title = item.title,
            )
        },
        searchQuery = state.searchQuery,
        endReached = state.endReached,
        refreshEvent = { event(PopularShowsContract.Event.Refresh) },
        loadNextItems = { loadNextItems() },
        selected = { id -> navigateToTvShow(id) }
    )
}

@Preview(backgroundColor = 0xffffffff, showBackground = false)
@Composable
fun PopularShowScreenPreview() {
    PopularShowsView(
        state = PopularShowsContract.State.Info(
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
        event = {},
        loadNextItems = {},
    )
}