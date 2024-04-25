package com.kounalem.moviedatabase.show.presentation.popular

import com.kounalem.moviedatabase.core.test.PaparazziScreenTest
import com.kounalem.moviedatabase.core.test.TestConfig
import org.junit.Test

internal class PopularShowsKtTest(config: TestConfig) : PaparazziScreenTest(config) {

    @Test
    fun popularShowsLoadingTest() {
        screenshotTest {
            PopularShowsView(
                navigateToTvShow = {},
                refresh = {},
                search = {},
                loadNextItems = {},
                state = PopularShowsContract.State.Loading,
            )
        }
    }

    @Test
    fun popularMoviesErrorTest() {
        screenshotTest {
            PopularShowsView(
                navigateToTvShow = {},
                refresh = {},
                search = {},
                loadNextItems = {},
                state = PopularShowsContract.State.Error("Epic failed"),
            )
        }
    }

    @Test
    fun popularMoviesSuccessTest() {
        screenshotTest {
            PopularShowsView(
                navigateToTvShow = {},
                refresh = {},
                search = {},
                loadNextItems = {},
                state = PopularShowsContract.State.Info(
                    shows = listOf(
                        PopularShowsContract.State.Info.Show(
                            id = 1,
                            posterPath = "https://encrypted-tbn3.gstatic.com/images?q=tbn:ANd9GcREEsN-qCwFIPE7-FglJVTrE0ijr7-VwggC6CXNtMLYtMnHWthZ",
                            title = "title",
                            overview = "overview",
                        )
                    ),
                    isRefreshing = true,
                    searchQuery = null,
                    endReached = false,
                    fetchingNewShows = false
                ),
            )
        }
    }

    @Test
    fun popularMoviesWhileLoadingSuccessTest() {
        screenshotTest {
            PopularShowsView(
                navigateToTvShow = {},
                loadNextItems = {},
                refresh = {},
                search = {},
                state = PopularShowsContract.State.Info(
                    shows = listOf(
                        PopularShowsContract.State.Info.Show(
                            id = 1,
                            posterPath = "https://encrypted-tbn3.gstatic.com/images?q=tbn:ANd9GcREEsN-qCwFIPE7-FglJVTrE0ijr7-VwggC6CXNtMLYtMnHWthZ",
                            title = "title",
                            overview = "overview",
                        )
                    ),
                    isRefreshing = true,
                    searchQuery = null,
                    endReached = false,
                    fetchingNewShows = true
                ),
            )
        }
    }
}