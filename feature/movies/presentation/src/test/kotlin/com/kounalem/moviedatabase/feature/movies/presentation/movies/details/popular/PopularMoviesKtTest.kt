package com.kounalem.moviedatabase.feature.movies.presentation.movies.details.popular

import com.kounalem.moviedatabase.core.test.PaparazziScreenTest
import com.kounalem.moviedatabase.core.test.TestConfig
import com.kounalem.moviedatabase.feature.movies.presentation.movies.popular.PopularMoviesContract
import com.kounalem.moviedatabase.feature.movies.presentation.movies.popular.PopularMoviesView
import org.junit.Test

internal class PopularMoviesKtTest(config: TestConfig) : PaparazziScreenTest(config) {

    @Test
    fun popularMoviesLoadingTest() {
        screenshotTest {
            PopularMoviesView(
                navigateToDetails = {},
                onSavedMoviesClicked = {},
                onRefresh = {},
                onSearchQueryChange = {},
                loadNextItems = {},
                state = PopularMoviesContract.State.Loading,
            )
        }
    }

    @Test
    fun popularMoviesErrorTest() {
        screenshotTest {
            PopularMoviesView(
                navigateToDetails = {},
                onSavedMoviesClicked = {},
                onRefresh = {},
                onSearchQueryChange = {},
                loadNextItems = {},
                state = PopularMoviesContract.State.Error("Epic failed"),
            )
        }
    }

    @Test
    fun popularMoviesSuccessTest() {
        screenshotTest {
            PopularMoviesView(
                navigateToDetails = {},
                onSavedMoviesClicked = {},
                onRefresh = {},
                onSearchQueryChange = {},
                loadNextItems = {},
                state = PopularMoviesContract.State.Info(
                    movies = listOf(
                        PopularMoviesContract.State.Info.Movie(
                            id = 1,
                            posterPath = "https://encrypted-tbn3.gstatic.com/images?q=tbn:ANd9GcREEsN-qCwFIPE7-FglJVTrE0ijr7-VwggC6CXNtMLYtMnHWthZ",
                            title = "title",
                            overview = "overview",
                        )
                    ),
                    isRefreshing = true,
                    searchQuery = null,
                    endReached = false,
                    fetchingNewMovies = false,
                    savedMoviesFilter = PopularMoviesContract.State.Info.SavedMoviesFilter("", false)
                ),
            )
        }
    }

    @Test
    fun popularMoviesWhileLoadingSuccessTest() {
        screenshotTest {
            PopularMoviesView(
                navigateToDetails = {},
                onSavedMoviesClicked = {},
                onRefresh = {},
                onSearchQueryChange = {},
                loadNextItems = {},
                state = PopularMoviesContract.State.Info(
                    movies = listOf(
                        PopularMoviesContract.State.Info.Movie(
                            id = 1,
                            posterPath = "https://encrypted-tbn3.gstatic.com/images?q=tbn:ANd9GcREEsN-qCwFIPE7-FglJVTrE0ijr7-VwggC6CXNtMLYtMnHWthZ",
                            title = "title",
                            overview = "overview",
                        )
                    ),
                    isRefreshing = true,
                    searchQuery = null,
                    endReached = false,
                    fetchingNewMovies = true,
                    savedMoviesFilter = PopularMoviesContract.State.Info.SavedMoviesFilter("", false)
                ),
            )
        }
    }
}