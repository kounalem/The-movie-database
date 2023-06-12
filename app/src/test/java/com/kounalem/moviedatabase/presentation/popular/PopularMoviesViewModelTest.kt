package com.kounalem.moviedatabase.presentation.popular

import app.cash.turbine.test
import com.kounalem.moviedatabase.CoroutineTestRule
import com.kounalem.moviedatabase.domain.MovieRepository
import com.kounalem.moviedatabase.domain.models.Movie
import com.kounalem.moviedatabase.domain.models.PopularMovies
import com.kounalem.moviedatabase.util.Resource
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
internal class PopularMoviesViewModelTest {

    @get:Rule
    val coroutineTestRule: CoroutineTestRule = CoroutineTestRule()

    @MockK
    private lateinit var movieRepository: MovieRepository

    private val viewModel by lazy {
        PopularMoviesViewModel(movieRepository)
    }

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)

        coEvery { movieRepository.nowPlaying(0) } returns
                flowOf(
                    Resource.Success(
                        PopularMovies(
                            id = 0,
                            page = 0,
                            movies = emptyList(),
                            totalPages = 1,
                            totalResults = 1,
                        )
                    )
                )
    }

    @Test
    fun `GIVEN success response WHEN onEvent THEN requests then update the state`() = runTest {
        val given = listOf(
            Movie(
                id = 1,
                posterPath = "",
                title = "title1",
                voteAverage = 1.0,
                overview = "overview1",
            ),
            Movie(
                id = 2,
                posterPath = "",
                title = "title2",
                voteAverage = 2.0,
                overview = "overview2",
            )
        )
        coEvery { movieRepository.search("hi") } returns
                flowOf(
                    Resource.Success(
                        given
                    )
                )


        viewModel.onEvent(PopularMoviesContract.MovieListingsEvent.OnSearchQueryChange("hi"))
        advanceTimeBy(600L)

        viewModel.state.test {
            assertEquals(
                actual = awaitItem().movies,
                expected = given
            )
        }
    }

    @Test
    fun `GIVEN loading response WHEN onEvent THEN requests then update the state`() = runTest {
        coEvery { movieRepository.search("hi") } returns flowOf(
            Resource.Loading()
        )
        viewModel.onEvent(PopularMoviesContract.MovieListingsEvent.OnSearchQueryChange("hi"))
        advanceTimeBy(600L)

        viewModel.state.test {
            assertTrue(
                awaitItem().isLoading,
            )
        }
    }

    @Test
    fun `GIVEN error response WHEN onEvent THEN requests then update the state`() = runTest {
        coEvery { movieRepository.search("hi") } returns flowOf(
            Resource.Error(
                "epic fail"
            )
        )
        viewModel.onEvent(PopularMoviesContract.MovieListingsEvent.OnSearchQueryChange("hi"))
        advanceTimeBy(600L)

        viewModel.state.test {
            assertEquals(
                actual = awaitItem().errorText,
                expected = "Data could not be retrieved."
            )
        }
    }


}