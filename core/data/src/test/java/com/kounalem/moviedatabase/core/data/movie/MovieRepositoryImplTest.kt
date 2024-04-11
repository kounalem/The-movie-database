package com.kounalem.moviedatabase.core.data.movie

import android.accounts.NetworkErrorException
import app.cash.turbine.test
import com.kounalem.moviedatabase.core.data.Outcome
import com.kounalem.moviedatabase.core.data.utils.CoroutineTestRule
import com.kounalem.moviedatabase.database.movie.LocalDataSource
import com.kounalem.moviedatabase.domain.models.Movie
import com.kounalem.moviedatabase.domain.models.MovieDescription
import com.kounalem.moviedatabase.domain.models.PopularMovies
import com.kounalem.moviedatabase.network.movies.ServerDataSource
import com.kounalem.moviedatabase.core.data.utils.onLatestItem
import com.kounalem.moviedatabase.network.NetworkResponse
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

internal class MovieRepositoryImplTest {
    @get:Rule
    val coroutineTestRule: CoroutineTestRule = CoroutineTestRule()

    @MockK
    private lateinit var server: ServerDataSource

    @MockK
    private lateinit var local: LocalDataSource

    private val repository by lazy {
        MovieRepositoryImpl(
            server = server, local = local, coroutineScope = coroutineTestRule.createTestScope()
        )
    }

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)
    }

    @Test
    fun `GIVEN server info available THEN get latest info`() = runTest {
        val given = listOf(mockk<Movie>())
        val popularMovies = NetworkResponse.Success<PopularMovies>(
            body = PopularMovies(
                movies = given,
                page = 1,
                totalPages = 1,
                totalResults = 1
            )
        )
        coEvery { server.nowPlaying(1) } returns flowOf(popularMovies)
        coEvery { local.getAllMovies() } returns flowOf(given)

        repository.movies.test {
            assertEquals(given, awaitItem().data)
        }
    }

    @Test
    fun `GIVEN server info available THEN get save the latest info`() = runTest {
        val given = listOf(mockk<Movie>())
        val popularMovies = NetworkResponse.Success<PopularMovies>(
            body = PopularMovies(
                movies = given,
                page = 1,
                totalPages = 1,
                totalResults = 1
            )
        )

        coEvery { local.getAllMovies() } returns flowOf(given)
        coEvery { server.nowPlaying(1) } returns flowOf(popularMovies)

        repository.movies.test {
            awaitItem()
            coVerify { local.saveMovieList(given) }
        }
    }

    @Test
    fun `GIVEN network error THEN requests then get local movies`() = runTest {
        val given = listOf(mockk<Movie>())
        coEvery { server.nowPlaying(1) } throws NetworkErrorException("")
        coEvery { local.getAllMovies() } returns flowOf(given)

        repository.movies.test {
            onLatestItem {
                assertTrue(it is Outcome.Success)
                assertEquals(it.data, given)
            }
        }
    }

    @Test
    fun `GIVEN local element does exist WHEN search THEN requests THEN return movie list`() =
        runTest {
            val given = mockk<Movie>()
            coEvery { local.getFilteredMovies("") } returns flowOf(listOf(given))

            repository.search("").collect {
                assertEquals(listOf(given), it)
            }
        }

    @Test
    fun `WHEN updateMovieFavStatus THEN update local element`() = runTest {
        repository.updateMovieFavStatus(1)

        coVerify { local.updateMovieFavStatus(1) }
    }

    @Test
    fun `GIVEN local info WHEN get movie by id THEN then return value`() = runTest {
        val given = MovieDescription(
            id = 1,
            originalTitle = "original_title",
            overview = "overview",
            posterPath = "https://image.tmdb.org/t/p/w342poster_path",
            title = "title",
            voteAverage = 0.0,
            isFavourite = false
        )
        coEvery { local.getMovieDescriptionById(1) } returns flowOf(given)

        repository.getMovieByIdObs(1).collect {
            assertEquals(given, it.data)
        }
    }

    @Test
    fun `GIVEN local info do not exist WHEN get movie by id THEN then return server value`() =
        runTest {
            val given = MovieDescription(
                id = 1,
                originalTitle = "original_title",
                overview = "overview",
                posterPath = "https://image.tmdb.org/t/p/w342poster_path",
                title = "title",
                voteAverage = 0.0,
                isFavourite = false
            )
            coEvery { local.getMovieDescriptionById(1) } returns flowOf(null)
            coEvery { server.getMovieById(1) } returns flowOf(NetworkResponse.Success<MovieDescription>(
                body = given
            ))

            repository.getMovieByIdObs(1).collect {
                assertEquals(given, it.data)
            }

            coVerify { local.saveMovieDescription(given) }
        }

    @Test
    fun `GIVEN local info do not exist and no network WHEN get movie by id THEN then fail`() =
        runTest {
            coEvery { local.getMovieDescriptionById(1) } returns flowOf(null)
            coEvery { server.getMovieById(1) } throws Throwable("epic fail")

            repository.getMovieByIdObs(1).collect {
                assertEquals(Outcome.Error("Movie info not available"), it)
            }
        }
}