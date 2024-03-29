package com.kounalem.moviedatabase.movies.popular

import app.cash.turbine.test
import com.kounalem.moviedatabase.core.data.Resource
import com.kounalem.moviedatabase.core.data.movie.MovieRepository
import com.kounalem.moviedatabase.core.domain.usecase.FilterMoviesUC
import com.kounalem.moviedatabase.domain.models.Movie
import com.kounalem.moviedatabase.movies.CoroutineTestRule
import com.kounalem.moviedatabase.paginator.Paginator
import com.kounalem.moviedatabase.paginator.PaginatorFactory
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
internal class PopularMoviesViewModelTest {

    @get:Rule
    val coroutineTestRule: CoroutineTestRule = CoroutineTestRule()

    @MockK
    private lateinit var movieRepository: MovieRepository

    @MockK
    private lateinit var filterMoviesUC: FilterMoviesUC

    @MockK
    private lateinit var paginator: Paginator<Int>
    private var paginatorFactory: PaginatorFactory<Int> = object : PaginatorFactory<Int> {
        override fun create(
            initialKey: Int,
            onRequest: suspend (nextKey: Int) -> Unit,
            getNextKey: suspend (currentKey: Int) -> Int
        ): Paginator<Int> = paginator
    }

    private val viewModel by lazy {
        PopularMoviesViewModel(
            repo = movieRepository,
            paginatorFactory = paginatorFactory,
            filterMoviesUC = filterMoviesUC,
        )
    }

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)

        coEvery { movieRepository.movies } returns flowOf(Resource.Success(emptyList()))
    }

    @Test
    fun `GIVEN movies THEN update the state with mapped info`() = runTest {
        val firstMovie = Movie(
            id = 1,
            posterPath = "",
            title = "title1",
            voteAverage = 2.0,
            overview = "overview1",
            date = 123
        )
        val secondMovie = Movie(
            id = 2,
            posterPath = "",
            title = "title2",
            voteAverage = 2.0,
            overview = "overview2",
            date = 123
        )
        coEvery { movieRepository.movies } returns flowOf(
            Resource.Success(
                listOf(
                    firstMovie,
                    secondMovie
                )
            )
        )

        viewModel.state.test {
            assertEquals(
                actual = awaitItem(),
                expected = PopularMoviesContract.State.Info(
                    movies = listOf(
                        PopularMoviesContract.State.Info.Movie(
                            id = 1,
                            posterPath = "",
                            title = "title1",
                            overview = "overview1",
                        ), PopularMoviesContract.State.Info.Movie(
                            id = 2,
                            posterPath = "",
                            title = "title2",
                            overview = "overview2",
                        )
                    ),
                    isRefreshing = false,
                    searchQuery = null,
                    endReached = false,
                    fetchingNewMovies = false,
                )
            )
        }
    }

    @Test
    fun `GIVEN search query THEN requests then update the state with the filtered info`() =
        runTest {
            val firstMovie = Movie(
                id = 1,
                posterPath = "",
                title = "title1",
                voteAverage = 2.0,
                overview = "overview1",
                date = 123
            )
            val secondMovie = Movie(
                id = 2,
                posterPath = "",
                title = "title2",
                voteAverage = 2.0,
                overview = "overview2",
                date = 123
            )
            val thirdMovie = Movie(
                id = 3,
                posterPath = "",
                title = "title3",
                voteAverage = 2.0,
                overview = "overview3",
                date = 123
            )

            val given = listOf(firstMovie, secondMovie)
            coEvery { filterMoviesUC.invoke("hi") } returns flowOf(given)
            coEvery { movieRepository.movies } returns flowOf(
                Resource.Success(
                    listOf(
                        firstMovie,
                        secondMovie,
                        thirdMovie
                    )
                )
            )

            viewModel.onEvent(PopularMoviesContract.MovieListingsEvent.OnSearchQueryChange("hi"))
            advanceTimeBy(600L)

            viewModel.state.test {
                assertEquals(
                    actual = awaitItem(),
                    expected = PopularMoviesContract.State.Info(
                        movies = listOf(
                            PopularMoviesContract.State.Info.Movie(
                                id = 1,
                                posterPath = "",
                                title = "title1",
                                overview = "overview1",
                            ), PopularMoviesContract.State.Info.Movie(
                                id = 2,
                                posterPath = "",
                                title = "title2",
                                overview = "overview2",
                            )
                        ),
                        isRefreshing = false,
                        searchQuery = "hi",
                        endReached = false,
                        fetchingNewMovies = false,
                    )
                )
            }
        }

    @Test
    fun `WHEN loadNextItems THEN state gets updated`() = runTest {
        coEvery { movieRepository.movies } returns flowOf(
            Resource.Success(
                listOf(
                    Movie(
                        id = 1,
                        posterPath = "",
                        title = "title",
                        voteAverage = 2.0,
                        overview = "overview",
                        date = 123
                    )
                )
            )
        )

        viewModel.loadNextItems()

        viewModel.state.test {
            assertEquals(
                actual = awaitItem(),
                expected = PopularMoviesContract.State.Info(
                    movies = listOf(
                        PopularMoviesContract.State.Info.Movie(
                            id = 1,
                            posterPath = "",
                            title = "title",
                            overview = "overview",
                        )
                    ),
                    isRefreshing = false,
                    searchQuery = null,
                    endReached = false,
                    fetchingNewMovies = false,
                )
            )
        }
    }

    @Test
    fun `GIVEN refresh THEN reset and  paginate`() = runTest {
        val movie = Movie(
            id = 1,
            posterPath = "",
            title = "title",
            voteAverage = 2.0,
            overview = "overview",
            date = 123
        )

        val given = listOf(movie)
        coEvery { filterMoviesUC.invoke("hi") } returns flowOf(given)
        coEvery { movieRepository.movies } returns flowOf(Resource.Success(listOf(movie)))

        viewModel.onEvent(PopularMoviesContract.MovieListingsEvent.Refresh)

        verify { paginator.reset() }
        coVerify { paginator.loadNextItems() }
    }
}