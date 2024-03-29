package com.kounalem.moviedatabase.movies.popular

import com.kounalem.moviedatabase.core.test.PaparazziScreenTest
import com.kounalem.moviedatabase.core.test.TestConfig
import org.junit.Test


internal class PopularMoviesKtTest(config: TestConfig) : PaparazziScreenTest(config) {

    @Test
    fun popularMoviesLoadingTest() {
        screenshotTest {
            PopularMoviesView(
                navigateToDetails = {},
                event = {},
                loadNextItems = {},
                state = PopularMoviesContract.State.Loading(
                    listOf(
                        PopularMoviesContract.State.Info.Movie(
                            id = 1,
                            posterPath = "https://encrypted-tbn3.gstatic.com/images?q=tbn:ANd9GcREEsN-qCwFIPE7-FglJVTrE0ijr7-VwggC6CXNtMLYtMnHWthZ",
                            title = "title",
                            overview = "overview",
                        )
                    )
                ),
            )
        }
    }

    @Test
    fun popularMoviesErrorTest() {
        screenshotTest {
            PopularMoviesView(
                navigateToDetails = {},
                event = {},
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
                event = {},
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
                    fetchingNewMovies = false
                ),
            )
        }
    }
}