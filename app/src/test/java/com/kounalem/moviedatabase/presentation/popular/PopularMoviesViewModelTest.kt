package com.kounalem.moviedatabase.presentation.popular

import app.cash.turbine.test
import com.kounalem.moviedatabase.CoroutineTestRule
import com.kounalem.moviedatabase.domain.MovieRepository
import com.kounalem.moviedatabase.domain.models.Movie
import com.kounalem.moviedatabase.domain.models.PopularMovies
import com.kounalem.moviedatabase.util.paginator.Paginator
import com.kounalem.moviedatabase.util.paginator.PaginatorFactory
import com.kounalem.moviedatabase.util.Resource
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
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

    @MockK
    private lateinit var paginatorFactory: PaginatorFactory<Int, Resource<PopularMovies>>

    @MockK
    private lateinit var paginator: Paginator<Int, Resource<PopularMovies>>
    private val viewModel by lazy {
        PopularMoviesViewModel(
            movieRepository = movieRepository,
            ioDispatcher = coroutineTestRule.testDispatcher,
            paginatorFactory = paginatorFactory
        )
    }

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)

        coEvery { movieRepository.nowPlaying(0) } returns
                flowOf(
                    Resource.Success(
                        PopularMovies(
                            page = 0,
                            movies = emptyList(),
                            totalPages = 1,
                            totalResults = 1,
                        )
                    )
                )

        every {
            paginatorFactory.create(
                initialKey = 1,
                onUpdate = any(),
                getNextKey = any(),
                onLoadUpdated = any(),
                onRequest = any(),
            )
        } returns paginator

    }

    @Test
    fun `GIVEN search query and success response WHEN onEvent THEN requests then update the state`() = runTest {
        val given = listOf(
            Movie(
                id = 1,
                posterPath = "",
                title = "title1",
                voteAverage = 1.0,
                overview = "overview1",
                date = 123,
            ),
            Movie(
                id = 2,
                posterPath = "",
                title = "title2",
                voteAverage = 2.0,
                overview = "overview2",
                date = 123
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
    fun `GIVEN search query and loading response WHEN onEvent THEN requests then update the state`() = runTest {
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
    fun `GIVEN search query and error response WHEN onEvent THEN requests then update the state`() = runTest {
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

    @Test
    fun `WHEN loadNextItems THEN state gets updated`() = runTest {
        coEvery { movieRepository.nowPlaying(any()) } returns flowOf(
            Resource.Success(
                PopularMovies(
                    page = 1,
                    movies = emptyList(),
                    totalPages = 1,
                    totalResults = 1,
                )
            )
        )
        coEvery { paginator.loadNextItems() } returns Unit

        viewModel.loadNextItems()

        viewModel.state.test {
            val item = awaitItem()
            assertEquals(
                actual = item.movies,
                expected = emptyList()
            )
            assertEquals(
                actual = item.page,
                expected = 1
            )
        }
    }

    @Test
    fun `GIVEN refresh WHEN onEvent THEN refresh the elements`() = runTest {
        coEvery { movieRepository.nowPlaying(any()) } returns flowOf(
            Resource.Success(
                PopularMovies(
                    page = 1,
                    movies = emptyList(),
                    totalPages = 1,
                    totalResults = 1,
                )
            )
        )
        coEvery { paginator.loadNextItems() } returns Unit

        viewModel.onEvent(PopularMoviesContract.MovieListingsEvent.Refresh)

        verify { viewModel.loadNextItems() }
    }
}