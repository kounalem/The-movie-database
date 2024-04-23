package com.kounalem.moviedatanase.core.ui.components

import android.content.res.Configuration
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
import androidx.compose.ui.tooling.preview.Preview
import com.airbnb.android.showkase.annotation.ShowkaseComposable
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.kounalem.moviedatanase.core.ui.PreviewBox
import com.kounalem.moviedatanase.core.ui.ShowkaseComposableGroup
import com.kounalem.moviedatanase.core.ui.annotations.ScreenPreview
import com.kounalem.moviedatanase.core.ui.model.ListItemModel
import com.kounalem.moviedatanase.core.ui.small

@Composable
fun PaginationList(
    modifier: Modifier = Modifier,
    refreshState: com.google.accompanist.swiperefresh.SwipeRefreshState,
    listState: LazyListState,
    isFetchingNewMovies: Boolean,
    items: List<ListItemModel>,
    searchQuery: String?,
    endReached: Boolean,
    refreshEvent: () -> Unit,
    loadNextItems: () -> Unit,
    selected: (Int) -> Unit
) {
    if (items.isEmpty()) return
    SwipeRefresh(
        modifier = modifier,
        state = refreshState,
        onRefresh = refreshEvent
    ) {
        LazyColumn(state = listState) {
            itemsIndexed(items) { index, item ->
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
                    }
                )
            }
            if (isFetchingNewMovies) {
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


@Composable
@ScreenPreview
private fun PaginationListLocalPreview() {
    PaginationList(
        refreshState = rememberSwipeRefreshState(true),
        listState = rememberLazyListState(),
        isFetchingNewMovies = false,
        items = listOf(
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
            )
        ),
        searchQuery = null,
        endReached = false,
        refreshEvent = {},
        loadNextItems = {},
        selected = {},
    )
}

@Composable
@ShowkaseComposable(name = "OutLinedTextField", group = ShowkaseComposableGroup.COMPONENTS)
fun PaginationListPreview() {
    PreviewBox {
        PaginationList(
            refreshState = rememberSwipeRefreshState(true),
            listState = rememberLazyListState(),
            isFetchingNewMovies = false,
            items = listOf(
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
                )
            ),
            searchQuery = null,
            endReached = false,
            refreshEvent = {},
            loadNextItems = {},
            selected = {},
        )
    }
}