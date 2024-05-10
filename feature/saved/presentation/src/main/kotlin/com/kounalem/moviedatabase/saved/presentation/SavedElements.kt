package com.kounalem.moviedatabase.saved.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kounalem.moviedatabase.core.ui.components.MovieListItem
import com.kounalem.moviedatabase.core.ui.components.MoviePill
import com.kounalem.moviedatabase.core.ui.components.MovieTopAppBar
import com.kounalem.moviedatabase.core.ui.theming.medium
import com.kounalem.moviedatabase.core.ui.model.ListItemModel
import com.kounalem.moviedatabase.core.ui.theming.small

@Composable
fun SavedElementsScreen() {
    val viewModel = hiltViewModel<SavedElementsViewModel>()
    val state = viewModel.uiState.collectAsStateWithLifecycle().value
    SavedElementsView(state = state)
}

@Composable
internal fun SavedElementsView(state: SavedElementsContract.State) {
    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        when (state) {
            is SavedElementsContract.State.Loading -> {
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

            is SavedElementsContract.State.Elements -> {
                val listState = rememberLazyListState()
                Column(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    MovieTopAppBar(
                        text = state.title,
                    )
                    LazyColumn(state = listState) {
                        items(state.info, key = { it.id }) { item ->
                            Box {
                                MovieListItem(
                                    model =
                                    ListItemModel(
                                        id = item.id,
                                        imagePath = item.posterPath,
                                        title = item.title,
                                        description = item.overview,
                                    ),
                                    selected = { },
                                )
                                MoviePill(
                                    text = item.type.name,
                                    modifier =
                                    Modifier
                                        .align(Alignment.TopEnd)
                                        .padding(top = medium, end = medium),
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(backgroundColor = 0xffffffff, showBackground = false)
@Composable
fun SavedElementsScreenPreview() {
    SavedElementsView(
        state =
        SavedElementsContract.State.Elements(
            "Saved elements",
            listOf(
                SavedElementsContract.State.Elements.Info(
                    id = 1,
                    posterPath = "path",
                    title = "title1",
                    overview = "overview",
                    type = SavedElementsContract.State.Elements.Type.Show,
                ),
                SavedElementsContract.State.Elements.Info(
                    id = 2,
                    posterPath = "path",
                    title = "title2",
                    overview = "overview2",
                    type = SavedElementsContract.State.Elements.Type.Movie,
                ),
                SavedElementsContract.State.Elements.Info(
                    id = 2,
                    posterPath = "path",
                    title = "title3",
                    overview = "overview3",
                    type = SavedElementsContract.State.Elements.Type.Movie,
                ),
            ),
        ),
    )
}
