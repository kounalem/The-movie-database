package com.kounalem.moviedatabase.saved.presentation

import com.kounalem.moviedatabase.core.test.PaparazziScreenTest
import com.kounalem.moviedatabase.core.test.TestConfig
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
internal class SavedElementsKtTest(config: TestConfig) : PaparazziScreenTest(config) {

    @Test
    fun savedElementsLoadingTest() {
        screenshotTest {
            SavedElementsView(
                state = SavedElementsContract.State.Loading,
            )
        }
    }

    @Test
    fun savedElementsSuccessTest() {
        screenshotTest {
            SavedElementsView(
                state = SavedElementsContract.State.Elements(
                    "Saved elements",
                    listOf(
                        SavedElementsContract.State.Elements.Info(
                            id = 1,
                            posterPath = "",
                            title = "title1",
                            overview = "overview",
                            type = SavedElementsContract.State.Elements.Type.Show,
                        ),
                        SavedElementsContract.State.Elements.Info(
                            id = 2,
                            posterPath = "",
                            title = "title2",
                            overview = "overview2",
                            type = SavedElementsContract.State.Elements.Type.Movie,
                        ),
                        SavedElementsContract.State.Elements.Info(
                            id = 2,
                            posterPath = "",
                            title = "title3",
                            overview = "overview3",
                            type = SavedElementsContract.State.Elements.Type.Movie,
                        ),
                    ),
                ),
            )
        }
    }

}