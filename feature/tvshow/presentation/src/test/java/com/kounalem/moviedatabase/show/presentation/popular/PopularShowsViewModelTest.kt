package com.kounalem.moviedatabase.show.presentation.popular

import app.cash.turbine.test
import com.kounalem.moviedatabase.core.test.CoroutineTestRule
import com.kounalem.moviedatabase.domain.models.TvShow
import com.kounalem.moviedatabase.repository.Outcome
import com.kounalem.moviedatabase.repository.TvShowRepository
import com.kounalem.moviedatabase.tvshow.domain.FilterShowsUC
import com.kounalem.moviedatanase.core.ui.paginator.Paginator
import com.kounalem.moviedatanase.core.ui.paginator.PaginatorFactory
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
internal class PopularShowsViewModelTest {

    @get:Rule
    val coroutineTestRule: CoroutineTestRule = CoroutineTestRule()

    @MockK
    private lateinit var repo: TvShowRepository

    @MockK
    private lateinit var filterShowsUc: FilterShowsUC

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
        PopularShowsViewModel(
            repo = repo,
            paginatorFactory = paginatorFactory,
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

    @Test
    fun `GIVEN refresh THEN reset and  paginate`() = runTest {
        val given = listOf(dummyTvShow1)
        coEvery { filterShowsUc.invoke("hi") } returns flowOf(given)
        coEvery { repo.tvShows } returns flowOf(Outcome.Success(listOf(dummyTvShow1)))

        viewModel.onEvent(PopularShowsContract.Event.Refresh)

        verify { paginator.reset() }
        coVerify { paginator.loadNextItems() }
    }

    private val dummyTvShow1 = TvShow(
        adult = false,
        id = 22980,
        originCountry = listOf("US"),
        originalLanguage = "en",
        originalName = "Watch What Happens Live with Andy Cohen",
        overview = "Bravo network executive Andy Cohen discusses pop culture topics with celebrities and reality show personalities.",
        popularity = 7285.549,
        posterPath = "https://image.tmdb.org/t/p/w342/onSD9UXfJwrMXWhq7UY7hGF2S1h.jpg",
        firstAirDate = "2009-07-16",
        name = "Watch What Happens Live with Andy Cohen",
        voteAverage = 5.078,
        voteCount = 45,
        languages = null,
        lastAirDate = null,
        seasons = null,
        type = null,
        isFavourite = false,
    )
    private val dummyTvShow2 = TvShow(
        adult = true,
        id = 22981,
        originCountry = listOf("US"),
        originalLanguage = "en",
        originalName = "Watch What Happens Live with Manolis Kounalakis",
        overview = "Bravo network executive Andy Cohen discusses pop culture topics with celebrities and reality show.",
        popularity = 7285.549,
        posterPath = "https://image.tmdb.org/t/p/w342/onSD9UXfJh.jpg",
        firstAirDate = "2009-07-17",
        name = "Watch What Happens Live withManolis Kounalakis",
        voteAverage = 5.079,
        voteCount = 47,
        languages = null,
        lastAirDate = null,
        seasons = null,
        type = null,
        isFavourite = false,
    )

}