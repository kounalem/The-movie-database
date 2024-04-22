package com.kounalem.moviedatabase.saved.presentation

import com.kounalem.moviedatabase.core.test.PaparazziScreenTest
import com.kounalem.moviedatabase.core.test.TestConfig
import org.junit.Test

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
                    "Saved Elements",
                    info = listOf(
                        SavedElementsContract.State.Elements.Info(
                            id = 1,
                            posterPath = "https://encrypted-tbn3.gstatic.com/images?q=tbn:ANd9GcREEsN-qCwFIPE7-FglJVTrE0ijr7-VwggC6CXNtMLYtMnHWthZ",
                            title = "title",
                            overview = "overview",
                            type = SavedElementsContract.State.Elements.Type.Movie,
                        )
                    ),
                ),
            )
        }
    }

}