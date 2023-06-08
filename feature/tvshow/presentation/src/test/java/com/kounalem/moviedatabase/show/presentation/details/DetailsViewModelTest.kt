package com.kounalem.moviedatabase.show.presentation.details

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.kounalem.moviedatabase.core.test.CoroutineTestRule
import com.kounalem.moviedatabase.domain.models.TvShow
import com.kounalem.moviedatabase.repository.Outcome
import com.kounalem.moviedatabase.repository.TvShowRepository
import com.kounalem.moviedatabase.show.presentation.details.navigation.Navigation
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

internal class DetailsViewModelTest {
    @get:Rule
    val coroutineTestRule: CoroutineTestRule = CoroutineTestRule()

    @MockK
    private lateinit var repository: TvShowRepository
    private val savedStateHandle: SavedStateHandle =
        SavedStateHandle(
            mapOf(Navigation.Details.DETAILS_ID to 1),
        )

    private val viewModel by lazy {
        DetailsViewModel(
            repo = repository,
            savedStateHandle = savedStateHandle,
        )
    }

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)
    }

    @Test
    fun `GIVEN repo returns error WHEN init THEN update the state`() =
        runTest {
            coEvery { repository.getTvShowByIdObs(1) } returns flowOf(Outcome.Error("epic fail"))

            viewModel.uiModels.test {
                assertEquals(
                    actual = awaitItem(),
                    expected = DetailsContract.State.Error("epic fail"),
                )
            }
        }

    @Test
    fun `GIVEN repo returns info WHEN init THEN update the state`() =
        runTest {
            coEvery { repository.getTvShowByIdObs(1) } returns
                flowOf(
                    Outcome.Success(
                        TvShow(
                            id = 1,
                            overview = "overview",
                            posterPath = "https://image.tmdb.org/t/p/w342poster_path",
                            isFavourite = false,
                            adult = false,
                            originalName = "Jocelyn Mills",
                            firstAirDate = "deseruisse",
                            name = "Alva Martin",
                            languages = listOf(),
                            lastAirDate = null,
                            seasons = listOf(),
                            type = null,
                        ),
                    ),
                )

            viewModel.uiModels.test {
                assertEquals(
                    actual = awaitItem(),
                    expected =
                        DetailsContract.State.Info(
                            title = "Alva Martin",
                            overview = "overview",
                            poster = "https://image.tmdb.org/t/p/w342poster_path",
                            isFavourite = false,
                            seasons = listOf(),
                            languages = listOf(),
                            firstAirDate = "deseruisse",
                            lastAirDate = "ON GOING",
                            type = null,
                        ),
                )
            }
        }

    @Test
    fun `WHEN favouriteAction event trigger repo call`() =
        runTest {
            viewModel.onFavouriteClicked()

            coVerify { repository.updateTvShowFavStatus(1) }
        }
}
