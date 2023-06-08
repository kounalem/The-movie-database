package com.kounalem.moviedatabaase.presentation.details

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.kounalem.moviedatabaase.CoroutineTestRule
import com.kounalem.moviedatabaase.domain.MovieRepository
import com.kounalem.moviedatabaase.domain.models.MovieDescription
import com.kounalem.moviedatabaase.util.Resource
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue


internal class DetailsViewModelTest {
    @get:Rule
    val coroutineTestRule: CoroutineTestRule = CoroutineTestRule()

    @MockK
    private lateinit var movieRepository: MovieRepository
    private val savedStateHandle: SavedStateHandle = SavedStateHandle(
        mapOf(
            "title" to "the title",
            "overview" to "the overview",
            "rate" to 1.0,
            "id" to 1,
        )
    )

    private val viewModel by lazy {
        DetailsViewModel(movieRepository, savedStateHandle)
    }

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)
    }

    @Test
    fun `GIVEN success response WHEN init THEN requests then update the state`() = runTest {
        val given = Resource.Success(
            MovieDescription(
                id = 1,
                originalTitle = "original title",
                overview = "overview",
                posterPath = "posterPath",
                title = "the title",
                voteAverage = 1.0,
                isFavourite = true,
            )
        )
        coEvery { movieRepository.getMovieById(1) } returns given

        viewModel.state.test {
            assertEquals(
                actual = awaitItem(),
                expected = DetailsContract.State(
                    isLoading = false,
                    title = "the title",
                    overview = "overview",
                    rate = "rate: 1.0",
                    poster = "posterPath",
                    errorText = null,
                    isFavourite = true,
                )
            )
        }
    }

    @Test
    fun `GIVEN loading response WHEN init THEN requests then update the state`() = runTest {
        coEvery { movieRepository.getMovieById(1) } returns Resource.Loading(
            true
        )

        viewModel.state.test {
            assertTrue(awaitItem().isLoading)
        }
    }

    @Test
    fun `GIVEN failed response WHEN init THEN requests then update the state`() = runTest {
        coEvery { movieRepository.getMovieById(1) } returns Resource.Error(
            "epic fail"
        )

        viewModel.state.test {
            assertEquals(
                actual = awaitItem().errorText,
                expected = "Data could not be retrieved."
            )
        }
    }

    @Test
    fun `GIVEN favouriteAction WHEN onEvent THEN requests then update repo and the state`() =
        runTest {
            coEvery { movieRepository.getMovieById(1) } returns Resource.Success(
                MovieDescription(
                    id = 1,
                    originalTitle = "original title",
                    overview = "overview",
                    posterPath = "posterPath",
                    title = "the title",
                    voteAverage = 1.0,
                    isFavourite = false,
                )
            )
            coEvery { movieRepository.favouriteAction(1, true) } returns Unit
            viewModel.onEvent(DetailsContract.MovieDetailsEvent.FavouriteAction)

            viewModel.state.test {
                assertTrue(
                    awaitItem().isFavourite,
                )
            }

            coVerify { movieRepository.favouriteAction(1, true) }
        }

}