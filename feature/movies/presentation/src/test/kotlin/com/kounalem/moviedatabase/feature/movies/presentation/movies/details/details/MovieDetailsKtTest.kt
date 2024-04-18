package com.kounalem.moviedatabase.feature.movies.presentation.movies.details.details

import com.kounalem.moviedatabase.core.test.PaparazziScreenTest
import com.kounalem.moviedatabase.core.test.TestConfig
import com.kounalem.moviedatabase.feature.movies.presentation.movies.details.DetailsContract
import com.kounalem.moviedatabase.feature.movies.presentation.movies.details.DetailsView
import org.junit.Test

internal class MovieDetailsKtTest(config: TestConfig) : PaparazziScreenTest(config) {

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
                popBackStack = {},
                onFavouriteClicked = {},
                state = DetailsContract.State.Info(
                    title = "title",
                    overview = "overview",
                    rate = "my rate",
                    poster = null,
                    isFavourite = true
                )
            )
        }
    }
}