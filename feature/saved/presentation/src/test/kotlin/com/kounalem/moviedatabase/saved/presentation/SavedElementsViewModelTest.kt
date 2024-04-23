package com.kounalem.moviedatabase.saved.presentation

import app.cash.turbine.test
import com.kounalem.moviedatabase.core.test.CoroutineTestRule
import com.kounalem.moviedatabase.saved.domain.FilterSavedUC
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import kotlin.test.Test
import kotlin.test.assertEquals

internal class SavedElementsViewModelTest {
    @MockK
    private lateinit var filterSavedInfoUseCase: FilterSavedUC

    @get:Rule
    val coroutineTestRule: CoroutineTestRule = CoroutineTestRule()

    private val viewModel by lazy {
        SavedElementsViewModel(filterSavedInfoUseCase)
    }

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)
    }

    @Test
    fun `GIVEN elements THEN update the state with mapped info`() = runTest {
        val firstMovie = FilterSavedUC.SavedElement.Movie(
            id = 1,
            title = "title movie 1",
            overview = "overview movie 1",
            image = "image movie1"
        )
        val secondMovie = FilterSavedUC.SavedElement.Movie(
            id = 2,
            title = "title movie 2",
            overview = "overview movie 2",
            image = "image movie 2"
        )
        val show = FilterSavedUC.SavedElement.TvShow(
            id = 1,
            title = "title show 1",
            overview = "overview show 1",
            image = "image show 1"
        )

        coEvery { filterSavedInfoUseCase.invoke() } returns flowOf(
            listOf(
                firstMovie, secondMovie, show
            )
        )

        viewModel.uiModels.test {
            assertEquals(
                actual = awaitItem(),
                expected = SavedElementsContract.State.Elements(
                    "Saved Elements",
                    listOf(
                        SavedElementsContract.State.Elements.Info(
                            id = 1,
                            title = "title movie 1",
                            overview = "overview movie 1",
                            posterPath = "image movie1",
                            type =  SavedElementsContract.State.Elements.Type.Movie
                        ),
                        SavedElementsContract.State.Elements.Info(
                            id = 2,
                            title = "title movie 2",
                            overview = "overview movie 2",
                            posterPath = "image movie 2",
                            type =  SavedElementsContract.State.Elements.Type.Movie
                        ),
                        SavedElementsContract.State.Elements.Info(
                            id = 1,
                            title = "title show 1",
                            overview = "overview show 1",
                            posterPath = "image show 1",
                            type = SavedElementsContract.State.Elements.Type.Show
                        )
                    )
                )
            )
        }
    }
}