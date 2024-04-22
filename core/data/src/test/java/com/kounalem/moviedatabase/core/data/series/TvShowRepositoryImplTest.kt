package com.kounalem.moviedatabase.core.data.series

import android.accounts.NetworkErrorException
import app.cash.turbine.test
import com.kounalem.moviedatabase.core.data.utils.CoroutineTestRule
import com.kounalem.moviedatabase.core.data.utils.onLatestItem
import com.kounalem.moviedatabase.database.movie.LocalDataSource
import com.kounalem.moviedatabase.domain.models.TvShow
import com.kounalem.moviedatabase.network.NetworkResponse
import com.kounalem.moviedatabase.network.series.SeriesDataSource
import com.kounalem.moviedatabase.repository.Outcome
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

internal class TvShowRepositoryImplTest {

    @get:Rule
    val coroutineTestRule: CoroutineTestRule = CoroutineTestRule()

    @MockK
    private lateinit var server: SeriesDataSource

    @MockK
    private lateinit var local: LocalDataSource

    private val repository by lazy {
        TvShowRepositoryImpl(
            server = server, local = local, coroutineScope = coroutineTestRule.createTestScope()
        )
    }

    private val dummyTvShow by lazy {
        TvShow(
            id = 1,
            overview = "overview",
            posterPath = "",
            name = "title",
            adult = false,
            originalName = "Darnell Baxter",
            firstAirDate = "parturient",
            languages = listOf(),
            lastAirDate = null,
            seasons = listOf(),
            type = null,
            isFavourite = false
        )
    }


    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)
    }

    @Test
    fun `GIVEN server info available THEN get latest info`() = runTest {
        val given = listOf(dummyTvShow)

        coEvery { local.getAllShows() } returns flowOf(given)

        repository.tvShows.test {
            kotlin.test.assertEquals(given, awaitItem().data)
        }
    }

    @Test
    fun `GIVEN server info available THEN get save the latest info`() = runTest {
        val given = listOf(dummyTvShow)

        coEvery { local.getAllShows() } returns flowOf(given)
        coEvery { server.popular(1) } returns flowOf(NetworkResponse.Success(given))

        repository.tvShows.test {
            awaitItem()
            coVerify { local.saveShowList(given) }
        }
    }

    @Test
    fun `GIVEN network error THEN requests then get local movies`() = runTest {
        val given = listOf(dummyTvShow)
        coEvery { server.popular(1) } throws NetworkErrorException("")
        coEvery { local.getAllShows() } returns flowOf(given)

        repository.tvShows.test {
            onLatestItem {
                kotlin.test.assertTrue(it is Outcome.Success)
                kotlin.test.assertEquals(it.data, given)
            }
        }
    }

    @Test
    fun `GIVEN local element does exist WHEN search THEN requests THEN return movie list`() =
        runTest {
            coEvery { local.getFilteredShows("") } returns flowOf(listOf(dummyTvShow))

            repository.search("").collect {
                kotlin.test.assertEquals(listOf(dummyTvShow), it)
            }
        }

    @Test
    fun `WHEN updateMovieFavStatus THEN update local element`() = runTest {
        repository.updateTvShowFavStatus(1)

        coVerify { local.updateShowFavStatus(1) }
    }

    @Test
    fun `GIVEN local info WHEN get movie by id THEN then return value`() = runTest {
        val given = dummyTvShow
        coEvery { local.getShowById(1) } returns flowOf(given)

        repository.getTvShowByIdObs(1).collect {
            kotlin.test.assertEquals(given, it.data)
        }
    }

    @Test
    fun `GIVEN local info do not exist WHEN get movie by id THEN then return server value`() =
        runTest {
            coEvery { local.getShowById(1) } returns flowOf(null)
            coEvery { server.getSeriesById(1) } returns flowOf(
                NetworkResponse.Success(body = dummyTvShow)
            )

            repository.getTvShowByIdObs(1).collect {
                kotlin.test.assertEquals(dummyTvShow, it.data)
            }

            coVerify { local.saveShowDescription(dummyTvShow) }
        }

    @Test
    fun `GIVEN local info do not exist and no network WHEN get movie by id THEN then fail`() =
        runTest {
            coEvery { local.getShowById(1) } returns flowOf(null)
            coEvery { server.getSeriesById(1) } throws Throwable("epic fail")

            repository.getTvShowByIdObs(1).collect {
                kotlin.test.assertEquals(Outcome.Error("Movie info not available"), it)
            }
        }
}