package com.kounalem.moviedatabase.feature.movies.presentation.movies.details.details

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.kounalem.moviedatabase.core.test.CoroutineTestRule
import com.kounalem.moviedatabase.repository.Outcome
import com.kounalem.moviedatabase.repository.MovieRepository
import com.kounalem.moviedatabase.domain.models.MovieDescription
import com.kounalem.moviedatabase.feature.movies.presentation.movies.details.DetailsContract
import com.kounalem.moviedatabase.feature.movies.presentation.movies.details.DetailsViewModel
import com.kounalem.moviedatabase.feature.movies.presentation.movies.details.navigation.Navigation
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
    private lateinit var repository: MovieRepository
    private val savedStateHandle: SavedStateHandle = SavedStateHandle(
        mapOf(Navigation.Details.DETAILS_ID to 1)
    )

    private val viewModel by lazy {
        DetailsViewModel(
            movieRepository = repository,
            savedStateHandle = savedStateHandle,
        )
    }

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)
    }

    @Test
    fun `GIVEN repo returns error WHEN  init THEN update the state`() = runTest {
        coEvery { repository.getMovieByIdObs(1) } returns flowOf(Outcome.Error("epic fail"))

        viewModel.state.test {
            assertEquals(
                actual = awaitItem(), expected = DetailsContract.State.Error("epic fail")
            )
        }
    }

    @Test
    fun `GIVEN repo returns info WHEN  init THEN update the state`() = runTest {
        coEvery { repository.getMovieByIdObs(1) } returns flowOf(
            Outcome.Success(
                MovieDescription(
                    id = 1,
                    originalTitle = "original_title",
                    overview = "overview",
                    posterPath = "https://image.tmdb.org/t/p/w342poster_path",
                    title = "title",
                    voteAverage = 0.0,
                    isFavourite = false
                )
            )
        )

        viewModel.state.test {
            assertEquals(
                actual = awaitItem(),
                expected = DetailsContract.State.Info(
                    title = "title",
                    overview = "overview",
                    rate = "Movie rating: 0.0",
                    poster = "https://image.tmdb.org/t/p/w342poster_path",
                    isFavourite = false
                )
            )
        }
    }

    @Test
    fun `WHEN favouriteAction event trigger repo call`() =
        runTest {
            viewModel.onEvent(DetailsContract.Event.FavouriteAction)

            coVerify { repository.updateMovieFavStatus(1) }
        }
}