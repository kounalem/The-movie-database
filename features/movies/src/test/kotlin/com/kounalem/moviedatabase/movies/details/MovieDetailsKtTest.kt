package com.kounalem.moviedatabase.movies.details

import com.kounalem.moviedatabase.core.test.PaparazziScreenTest
import com.kounalem.moviedatabase.core.test.TestConfig
import org.junit.Test


internal class MovieDetailsKtTest(config: TestConfig) : PaparazziScreenTest(config) {

    @Test
    fun movieDetailsLoadingTest() {
        screenshotTest {
            DetailsView(
                popBackStack = {},
                event = {},
                state = DetailsContract.State.Loading,
            )
        }
    }

    @Test
    fun movieDetailsErrorTest() {
        screenshotTest {
            DetailsView(
                popBackStack = {},
                event = {},
                state = DetailsContract.State.Error("Epic failed"),
            )
        }
    }

    @Test
    fun movieDetailsSuccessTest() {
        screenshotTest {
            DetailsView(
                popBackStack = {},
                event = {},
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