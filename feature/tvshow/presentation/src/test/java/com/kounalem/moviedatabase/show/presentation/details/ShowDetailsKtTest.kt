package com.kounalem.moviedatabase.show.presentation.details

import com.kounalem.moviedatabase.core.test.PaparazziScreenTest
import com.kounalem.moviedatabase.core.test.TestConfig
import org.junit.Test

internal class ShowDetailsKtTest(config: TestConfig) : PaparazziScreenTest(config) {

    @Test
    fun movieDetailsLoadingTest() {
        screenshotTest {
            DetailsView(
                popBackStack = {},
                onFavouriteClicked = {},
                state = DetailsContract.State.Loading,
            )
        }
    }

    @Test
    fun movieDetailsErrorTest() {
        screenshotTest {
            DetailsView(
                popBackStack = {},
                onFavouriteClicked = {},
                state = DetailsContract.State.Error("Epic failed"),
            )
        }
    }

    @Test
    fun movieDetailsSuccessTest() {
        screenshotTest {
            DetailsView(
                state =
                DetailsContract.State.Info(
                    title = "title",
                    overview = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
                    poster = null,
                    isFavourite = true,
                    seasons =
                    listOf(
                        DetailsContract.State.Info.Season(
                            airDate = "aeque",
                            episodeCount = 5438,
                            id = 1121,
                            name = "Marcy Rivera",
                            overview = "definitiones",
                            posterPath = null,
                            seasonNumber = 6885,
                            voteAverage = 1912,
                        ),
                        DetailsContract.State.Info.Season(
                            airDate = "vehicula",
                            episodeCount = 7174,
                            id = 6749,
                            name = "Earnestine Campos",
                            overview = "ornatus",
                            posterPath = null,
                            seasonNumber = 6168,
                            voteAverage = 1389,
                        ),
                        DetailsContract.State.Info.Season(
                            airDate = "saepe",
                            episodeCount = 6421,
                            id = 9141,
                            name = "Marci Craig",
                            overview = "legimus",
                            posterPath = null,
                            seasonNumber = 7029,
                            voteAverage = 8033,
                        ),
                        DetailsContract.State.Info.Season(
                            airDate = "mauris",
                            episodeCount = 2761,
                            id = 8413,
                            name = "Terry Olsen",
                            overview = "decore",
                            posterPath = null,
                            seasonNumber = 2025,
                            voteAverage = 3260,
                        ),
                    ),
                    languages = listOf("English", "Greek"),
                    lastAirDate = "ON GOING",
                    type = null,
                    firstAirDate = "21-2-22",
                ),
                onFavouriteClicked = {},
                popBackStack = {},
            )
        }
    }
}