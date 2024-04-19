package com.kounalem.moviedatabase.show.presentation.popular

import app.cash.turbine.test
import com.kounalem.moviedatabase.core.test.CoroutineTestRule
import com.kounalem.moviedatabase.domain.models.TvShow
import com.kounalem.moviedatabase.repository.Outcome
import com.kounalem.moviedatabase.repository.TvShowRepository
import com.kounalem.moviedatabase.tvshow.domain.FilterShowsUC
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
internal class PopularShowsViewModelTest {

    @get:Rule
    val coroutineTestRule: CoroutineTestRule = CoroutineTestRule()

    @MockK
    private lateinit var repo: TvShowRepository

    @MockK
    private lateinit var filterShowsUc: FilterShowsUC

    private val viewModel by lazy {
        PopularShowsViewModel(
            repo = repo,
            filterShowUc = filterShowsUc,
        )
    }

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)

        coEvery { repo.tvShows } returns flowOf(Outcome.Success(emptyList()))
    }

    @Test
    fun `GIVEN shows THEN update the state with mapped info`() = runTest {
        coEvery { repo.tvShows } returns flowOf(
            Outcome.Success(
                listOf(
                    dummyTvShow1,
                    dummyTvShow2
                )
            )
        )

        viewModel.state.test {
            assertEquals(
                actual = awaitItem(),
                expected = PopularShowsContract.State.Info(
                    shows = listOf(
                        PopularShowsContract.State.Info.Show(
                            id = 22980,
                            posterPath = "https://image.tmdb.org/t/p/w342/onSD9UXfJwrMXWhq7UY7hGF2S1h.jpg",
                            title = "Watch What Happens Live with Andy Cohen",
                            overview = "Bravo network executive Andy Cohen discusses pop culture topics with celebrities and reality show personalities.",
                        ),
                        PopularShowsContract.State.Info.Show(
                            id = 22981,
                            posterPath = "https://image.tmdb.org/t/p/w342/onSD9UXfJh.jpg",
                            title = "Watch What Happens Live withManolis Kounalakis",
                            overview = "Bravo network executive Andy Cohen discusses pop culture topics with celebrities and reality show.",
                        )
                    ),
                    isRefreshing = false,
                    searchQuery = null,
                    endReached = false,
                    fetchingNewShows = false,
                )
            )
        }
    }

    @Test
    fun `GIVEN search query THEN requests then update the state with the filtered info`() =
        runTest {
            val given = listOf(dummyTvShow1, dummyTvShow2)
            coEvery { filterShowsUc.invoke("hi") } returns flowOf(given)
            coEvery { repo.tvShows } returns flowOf(
                Outcome.Success(
                    listOf(
                        dummyTvShow1,
                        dummyTvShow2,
                    )
                )
            )

            viewModel.onEvent(PopularShowsContract.Event.OnSearchQueryChange("hi"))
            advanceTimeBy(600L)

            viewModel.state.test {
                assertEquals(
                    actual = awaitItem(),
                    expected = PopularShowsContract.State.Info(
                        shows = listOf(
                            PopularShowsContract.State.Info.Show(
                                id = 22980,
                                posterPath = "https://image.tmdb.org/t/p/w342/onSD9UXfJwrMXWhq7UY7hGF2S1h.jpg",
                                title = "Watch What Happens Live with Andy Cohen",
                                overview = "Bravo network executive Andy Cohen discusses pop culture topics with celebrities and reality show personalities.",
                            ),
                            PopularShowsContract.State.Info.Show(
                                id = 22981,
                                posterPath = "https://image.tmdb.org/t/p/w342/onSD9UXfJh.jpg",
                                title = "Watch What Happens Live withManolis Kounalakis",
                                overview = "Bravo network executive Andy Cohen discusses pop culture topics with celebrities and reality show.",
                            )
                        ),
                        isRefreshing = false,
                        searchQuery = "hi",
                        endReached = false,
                        fetchingNewShows = false,
                    )
                )
            }
        }

    @Test
    fun `WHEN loadNextItems THEN state gets updated`() = runTest {
        coEvery { repo.tvShows } returns flowOf(Outcome.Success(listOf(dummyTvShow1)))

        viewModel.loadNextItems()

        viewModel.state.test {
            assertEquals(
                actual = awaitItem(),
                expected = PopularShowsContract.State.Info(
                    shows = listOf(
                        PopularShowsContract.State.Info.Show(
                            id = 22980,
                            posterPath = "https://image.tmdb.org/t/p/w342/onSD9UXfJwrMXWhq7UY7hGF2S1h.jpg",
                            title = "Watch What Happens Live with Andy Cohen",
                            overview = "Bravo network executive Andy Cohen discusses pop culture topics with celebrities and reality show personalities.",
                        ),
                    ),
                    isRefreshing = false,
                    searchQuery = null,
                    endReached = false,
                    fetchingNewShows = false,
                )
            )
        }
    }

    private val dummyTvShow1 = TvShow(
        adult = false,
        id = 22980,
        originalName = "Watch What Happens Live with Andy Cohen",
        overview = "Bravo network executive Andy Cohen discusses pop culture topics with celebrities and reality show personalities.",
        posterPath = "https://image.tmdb.org/t/p/w342/onSD9UXfJwrMXWhq7UY7hGF2S1h.jpg",
        firstAirDate = "2009-07-16",
        name = "Watch What Happens Live with Andy Cohen",
        languages = null,
        lastAirDate = null,
        seasons = null,
        type = null,
        isFavourite = false,
    )
    private val dummyTvShow2 = TvShow(
        adult = true,
        id = 22981,
        originalName = "Watch What Happens Live with Manolis Kounalakis",
        overview = "Bravo network executive Andy Cohen discusses pop culture topics with celebrities and reality show.",
        posterPath = "https://image.tmdb.org/t/p/w342/onSD9UXfJh.jpg",
        firstAirDate = "2009-07-17",
        name = "Watch What Happens Live withManolis Kounalakis",
        languages = null,
        lastAirDate = null,
        seasons = null,
        type = null,
        isFavourite = false,
    )

}