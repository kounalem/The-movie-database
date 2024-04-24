package com.kounalem.moviedatabase.feature.movies.presentation.movies.details.popular

import app.cash.turbine.test
import com.kounalem.moviedatabase.core.test.CoroutineTestRule
import com.kounalem.moviedatabase.domain.models.Movie
import com.kounalem.moviedatabase.feature.movies.domain.usecase.FilterMoviesUC
import com.kounalem.moviedatabase.feature.movies.domain.usecase.GetMostPopularMoviesUC
import com.kounalem.moviedatabase.feature.movies.presentation.movies.popular.PopularMoviesContract
import com.kounalem.moviedatabase.feature.movies.presentation.movies.popular.PopularMoviesViewModel
import com.kounalem.moviedatabase.repository.MovieRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
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
    private lateinit var popularMoviesUC: GetMostPopularMoviesUC

    private val viewModel by lazy {
        PopularMoviesViewModel(
            repo = movieRepository,
            filterMoviesUC = filterMoviesUC,
            popularMoviesUC = popularMoviesUC,
        )
    }

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)

        coEvery { popularMoviesUC.movies } returns flowOf(LinkedHashMap())
    }

    @Test
    fun `GIVEN movies THEN update the state with mapped info`() =
        runTest {
            val firstMovie =
                Movie(
                    id = 1,
                    posterPath = "",
                    title = "title1",
                    voteAverage = 2.0,
                    overview = "overview1",
                    date = 123,
                    isFavourite = false,
                    originalTitle = "",
                    page = 1,
                )
            val secondMovie =
                Movie(
                    id = 2,
                    posterPath = "",
                    title = "title2",
                    voteAverage = 2.0,
                    overview = "overview2",
                    date = 123,
                    isFavourite = false,
                    originalTitle = "",
                    page = 1,
                )
            val popularMovies =
                listOf(
                    firstMovie,
                    secondMovie,
                )
            coEvery { popularMoviesUC.movies } returns
                flowOf(
                    LinkedHashMap<Int, Movie>().apply {
                        popularMovies.forEach { put(it.id, it) }
                    },
                )

            viewModel.uiModels.test {
                assertEquals(
                    actual = awaitItem(),
                    expected =
                        PopularMoviesContract.State.Info(
                            movies =
                                listOf(
                                    PopularMoviesContract.State.Info.Movie(
                                        id = 1,
                                        posterPath = "",
                                        title = "title1",
                                        overview = "overview1",
                                    ),
                                    PopularMoviesContract.State.Info.Movie(
                                        id = 2,
                                        posterPath = "",
                                        title = "title2",
                                        overview = "overview2",
                                    ),
                                ),
                            isRefreshing = false,
                            searchQuery = null,
                            endReached = false,
                            fetchingNewMovies = false,
                            savedMoviesFilter =
                                PopularMoviesContract.State.Info.SavedMoviesFilter(
                                    filterText = "All movies",
                                    isFiltering = false,
                                ),
                        ),
                )
            }
        }

    @Test
    fun `GIVEN search query THEN requests then update the state with the filtered info`() =
        runTest {
            val firstMovie =
                Movie(
                    id = 1,
                    posterPath = "",
                    title = "title1",
                    voteAverage = 2.0,
                    overview = "overview1",
                    date = 123,
                    isFavourite = false,
                    originalTitle = "",
                    page = 1,
                )
            val secondMovie =
                Movie(
                    id = 2,
                    posterPath = "",
                    title = "title2",
                    voteAverage = 2.0,
                    overview = "overview2",
                    date = 123,
                    isFavourite = false,
                    originalTitle = "",
                    page = 1,
                )
            val thirdMovie =
                Movie(
                    id = 3,
                    posterPath = "",
                    title = "title3",
                    voteAverage = 2.0,
                    overview = "overview3",
                    date = 123,
                    isFavourite = false,
                    originalTitle = "",
                    page = 1,
                )

            val given = listOf(firstMovie, secondMovie)
            val popularMovies =
                listOf(
                    firstMovie,
                    secondMovie,
                    thirdMovie,
                )
            coEvery { filterMoviesUC.invoke("hi") } returns flowOf(given)
            coEvery { popularMoviesUC.movies } returns
                flowOf(
                    LinkedHashMap<Int, Movie>().apply {
                        popularMovies.forEach { put(it.id, it) }
                    },
                )

            viewModel.onSearchQueryChange("hi")
            advanceTimeBy(600L)

            viewModel.uiModels.test {
                assertEquals(
                    actual = awaitItem(),
                    expected =
                        PopularMoviesContract.State.Info(
                            movies =
                                listOf(
                                    PopularMoviesContract.State.Info.Movie(
                                        id = 1,
                                        posterPath = "",
                                        title = "title1",
                                        overview = "overview1",
                                    ),
                                    PopularMoviesContract.State.Info.Movie(
                                        id = 2,
                                        posterPath = "",
                                        title = "title2",
                                        overview = "overview2",
                                    ),
                                ),
                            isRefreshing = false,
                            searchQuery = "hi",
                            endReached = false,
                            fetchingNewMovies = false,
                            savedMoviesFilter =
                                PopularMoviesContract.State.Info.SavedMoviesFilter(
                                    filterText = "All movies",
                                    isFiltering = false,
                                ),
                        ),
                )
            }
        }

    @Test
    fun `WHEN loadNextItems THEN state gets updated`() =
        runTest {
            val movie =
                Movie(
                    id = 1,
                    posterPath = "",
                    title = "title",
                    voteAverage = 2.0,
                    overview = "overview",
                    date = 123,
                    isFavourite = false,
                    originalTitle = "",
                    page = 1,
                )
            coEvery { popularMoviesUC.movies } returns
                flowOf(
                    linkedMapOf(movie.id to movie),
                )

            viewModel.loadNextItems()

            viewModel.uiModels.test {
                assertEquals(
                    actual = awaitItem(),
                    expected =
                        PopularMoviesContract.State.Info(
                            movies =
                                listOf(
                                    PopularMoviesContract.State.Info.Movie(
                                        id = 1,
                                        posterPath = "",
                                        title = "title",
                                        overview = "overview",
                                    ),
                                ),
                            isRefreshing = false,
                            searchQuery = null,
                            endReached = false,
                            fetchingNewMovies = false,
                            savedMoviesFilter =
                                PopularMoviesContract.State.Info.SavedMoviesFilter(
                                    filterText = "All movies",
                                    isFiltering = false,
                                ),
                        ),
                )
            }
        }
}
