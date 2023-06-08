package com.kounalem.moviedatabase.core.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.kounalem.moviedatabase.core.ui.annotations.ScreenPreview
import com.kounalem.moviedatabase.core.ui.model.ListItemModel
import com.kounalem.moviedatabase.core.ui.theming.small
import com.google.accompanist.swiperefresh.SwipeRefreshState

@Composable
fun MoviePaginationList(
    modifier: Modifier = Modifier,
    refreshState: SwipeRefreshState,
    listState: LazyListState,
    isFetchingNewMovies: Boolean,
    items: List<ListItemModel>,
    searchQuery: String?,
    endReached: Boolean,
    refreshEvent: () -> Unit,
    loadNextItems: () -> Unit,
    selected: (Int) -> Unit,
) {
    if (items.isEmpty()) return
    SwipeRefresh(
        modifier = modifier,
        state = refreshState,
        onRefresh = refreshEvent,
    ) {
        LazyColumn(state = listState) {
            itemsIndexed(
                items = items,
                key = { index, item -> item.id },
                contentType = { _, item -> item.title },
            ) { index, item ->
                val isLastItem = index == (items.size - 1)
                val shouldLoadNextItems =
                    isLastItem && !endReached && searchQuery.isNullOrEmpty() && !isFetchingNewMovies
                if (shouldLoadNextItems) {
                    loadNextItems()
                }

                MovieListItem(
                    model = item,
                    selected = { id: Int ->
                        selected(id)
                    },
                )
            }
            if (isFetchingNewMovies) {
                item {
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
            }
        }
    }
}

@Composable
@ScreenPreview
private fun PaginationListLocalPreview() {
    MoviePaginationList(
        refreshState = rememberSwipeRefreshState(true),
        listState = rememberLazyListState(),
        isFetchingNewMovies = false,
        items =
        listOf(
            ListItemModel(
                id = 0,
                imagePath = "",
                title = "title",
                description = "description",
            ),
            ListItemModel(
                id = 0,
                imagePath = "",
                title = "title",
                description = "description",
            ),
            ListItemModel(
                id = 0,
                imagePath = "",
                title = "title",
                description = "description",
            ),
            ListItemModel(
                id = 0,
                imagePath = "",
                title = "title",
                description = "description",
            ),
        ),
        searchQuery = null,
        endReached = false,
        refreshEvent = {},
        loadNextItems = {},
        selected = {},
    )
}
