package com.kounalem.moviedatabase.saved.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kounalem.moviedatabase.core.ui.components.MovieListItem
import com.kounalem.moviedatabase.core.ui.components.Pill
import com.kounalem.moviedatabase.core.ui.medium
import com.kounalem.moviedatabase.core.ui.model.ListItemModel
import com.kounalem.moviedatabase.core.ui.small

@Composable
fun SavedElementsScreen() {
    val viewModel = hiltViewModel<SavedElementsViewModel>()
    val state = viewModel.uiState.collectAsStateWithLifecycle().value
    SavedElementsView(state = state)
}

@OptIn(ExperimentalMaterial3Api::class)
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
                    TopAppBar(
                        title = { Text(text = state.title) },
                        colors =
                            TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                titleContentColor = MaterialTheme.colorScheme.onPrimary,
                                navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                                actionIconContentColor = MaterialTheme.colorScheme.onSecondary,
                            ),
                    )
                    LazyColumn(state = listState) {
                        itemsIndexed(state.info) { _, item ->
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
                                Pill(
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
