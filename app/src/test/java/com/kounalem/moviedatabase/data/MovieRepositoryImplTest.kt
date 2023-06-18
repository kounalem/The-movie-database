package com.kounalem.moviedatabase.data

import android.accounts.NetworkErrorException
import app.cash.turbine.test
import com.kounalem.moviedatabase.data.db.LocalDataSource
import com.kounalem.moviedatabase.data.remote.ServerDataSource
import com.kounalem.moviedatabase.domain.models.Movie
import com.kounalem.moviedatabase.domain.models.MovieDescription
import com.kounalem.moviedatabase.domain.models.PopularMovies
import com.kounalem.moviedatabase.util.Resource
import com.kounalem.moviedatabase.utils.onLatestItem
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.lang.Exception
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class MovieRepositoryImplTest {

    @MockK
    private lateinit var serverDataSource: ServerDataSource

    @MockK
    private lateinit var localDataSource: LocalDataSource

    private val SUT by lazy {
        MovieRepositoryImpl(
            serverDataSource = serverDataSource,
            localDataSource = localDataSource,
        )
    }

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)
    }

    @Test
    fun `WHEN now playing THEN requests then get network favourite movieS`() = runTest {
        val popularMovies = mockk<PopularMovies>()
        coEvery { serverDataSource.nowPlaying(1) } returns popularMovies

        SUT.nowPlaying(1).test {
            assertTrue(awaitItem() is Resource.Loading)
            onLatestItem {
                assertTrue(it is Resource.Success)
                assertEquals(
                    it.data, popularMovies
                )
            }
        }
    }

    @Test
    fun `GIVEN network error WHEN now playing THEN requests then get local favourite movies`() =
        runTest {
            val movie = mockk<Movie>()
            val popularMovies = mockk<PopularMovies> {
                every { page } returns 1
                every { movies } returns listOf(movie)
            }

            coEvery { serverDataSource.nowPlaying(1) } throws NetworkErrorException("")
            coEvery { localDataSource.nowPlaying(1) } returns popularMovies

            SUT.nowPlaying(1).test {
                assertTrue(awaitItem() is Resource.Loading)
                onLatestItem {
                    assertTrue(it is Resource.Success)
                    assertEquals(
                        it.data, popularMovies
                    )
                }
            }
        }

    @Test
    fun `GIVEN network error and local element does not exist WHEN now playing THEN resource error`() =
        runTest {
            coEvery { serverDataSource.nowPlaying(1) } throws NetworkErrorException("")
            coEvery { localDataSource.nowPlaying(1) } throws Exception("")

            SUT.nowPlaying(1).test {
                assertTrue(awaitItem() is Resource.Loading)
                onLatestItem {
                    assertTrue(it is Resource.Error)
                }
            }
        }

    @Test
    fun `GIVEN local element does not exist WHEN search THEN requests THEN resource error`() =
        runTest {
            coEvery { localDataSource.getFilteredMovies("") } throws Exception("")

            SUT.search("").test {
                assertTrue(awaitItem() is Resource.Loading)
                onLatestItem {
                    assertTrue(it is Resource.Error)
                }
            }
        }

    @Test
    fun `GIVEN local element does exist WHEN search THEN requests THEN return movie list`() =
        runTest {
            val movie = mockk<Movie>(relaxed = true)
            coEvery { localDataSource.getFilteredMovies("") } returns listOf(movie)

            SUT.search("").test {
                assertTrue(awaitItem() is Resource.Loading)
                onLatestItem {
                    assertEquals(
                        it.data ?: emptyList(), listOf(
                            movie
                        )
                    )
                }
            }
        }

    @Test
    fun `WHEN favouriteAction THEN update local element`() =
        runTest {
            val info = MovieDescription(
                originalTitle = "originalTitle",
                overview = "overview",
                title = "title",
                id = 1,
                posterPath = "posterPath",
                isFavourite = true,
                voteAverage = 1.0
            )
            coEvery { localDataSource.getMovieDescriptionById(1) } returns
                    info.copy(isFavourite = false)

            SUT.favouriteAction(1, false)
            coVerify { localDataSource.saveMovieDescription(info.copy(isFavourite = false)) }
        }

    @Test
    fun `GIVEN local element WHEN getMovieById THEN requests THEN return movie description`() =
        runTest {
            val info = mockk<MovieDescription> {
                every { id } returns 1
            }
            coEvery { localDataSource.getMovieDescriptionById(1) } returns info
            val result = SUT.getMovieById(1)

            assertEquals(
                result.data,
                info
            )
        }

}