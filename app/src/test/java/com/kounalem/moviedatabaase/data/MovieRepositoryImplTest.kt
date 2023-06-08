package com.kounalem.moviedatabaase.data

import android.accounts.NetworkErrorException
import app.cash.turbine.test
import com.kounalem.moviedatabaase.CoroutineTestRule
import com.kounalem.moviedatabaase.data.db.LocalDataSource
import com.kounalem.moviedatabaase.data.db.models.MovieDAO
import com.kounalem.moviedatabaase.data.db.models.MovieDescriptionDAO
import com.kounalem.moviedatabaase.data.db.models.PopularMoviesDAO
import com.kounalem.moviedatabaase.data.remote.ServerDataSource
import com.kounalem.moviedatabaase.data.remote.models.MovieDTO
import com.kounalem.moviedatabaase.data.remote.models.PopularMoviesDTO
import com.kounalem.moviedatabaase.domain.models.Movie
import com.kounalem.moviedatabaase.util.Resource
import com.kounalem.moviedatabaase.utils.onLatestItem
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.lang.Exception
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class MovieRepositoryImplTest {
    @get:Rule
    val coroutineTestRule: CoroutineTestRule = CoroutineTestRule()

    @MockK
    private lateinit var serverDataSource: ServerDataSource

    @MockK
    private lateinit var localDataSource: LocalDataSource

    private val SUT by lazy {
        MovieRepositoryImpl(serverDataSource, localDataSource)
    }

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)
    }

    @Test
    fun `WHEN now playing THEN requests then get network favourite movie`() = runTest {
        val dao = PopularMoviesDTO(
            page = 0,
            movies = listOf(
                MovieDTO(
                    originalTitle = "originalTitle",
                    overview = "overview",
                    title = "title",
                    id = 1,
                    poster_path = "posterPath",
                    voteAverage = 1.0
                )
            ),
            totalPages = 5, totalResults = 20,
        )
        coEvery { serverDataSource.nowPlaying(1) } returns dao
        coEvery { localDataSource.saveMovie(any<MovieDAO>()) } returns Unit
        coEvery { localDataSource.saveMovie(any<PopularMoviesDAO>()) } returns Unit

        SUT.nowPlaying(1).test {
            assertTrue(awaitItem() is Resource.Loading)
            onLatestItem {
                assertTrue(it is Resource.Success)
                assertEquals(
                    it.data?.movies ?: emptyList(), listOf(
                        Movie(
                            title = "title",
                            overview = "overview",
                            id = 1,
                            posterPath = "https://image.tmdb.org/t/p/w342posterPath",
                            voteAverage = 1.0
                        )
                    )
                )
            }
        }
    }

    @Test
    fun `GIVEN network error WHEN now playing THEN requests then get local favourite movies`() =
        runTest {
            val dao = listOf(
                PopularMoviesDAO(
                    page = 0,
                    movies = arrayListOf(
                        MovieDAO(
                            originalTitle = "originalTitle",
                            overview = "overview",
                            title = "title",
                            id = 1,
                            posterPath = "posterPath",
                            voteAverage = 1.0
                        )
                    ),
                    totalPages = 5, totalResults = 20,
                )
            )
            coEvery { serverDataSource.nowPlaying(1) } throws NetworkErrorException("")
            coEvery { localDataSource.nowPlaying() } returns dao
            coEvery { localDataSource.saveMovie(any<MovieDAO>()) } returns Unit
            coEvery { localDataSource.saveMovie(any<PopularMoviesDAO>()) } returns Unit

            SUT.nowPlaying(1).test {
                assertTrue(awaitItem() is Resource.Loading)
                onLatestItem {
                    assertTrue(it is Resource.Success)
                    assertEquals(
                        it.data?.movies ?: emptyList(), listOf(
                            Movie(
                                title = "title",
                                overview = "overview",
                                id = 1,
                                posterPath = "https://image.tmdb.org/t/p/w342posterPath",
                                voteAverage = 1.0
                            )
                        )
                    )
                }
            }
        }

    @Test
    fun `GIVEN network error and local element does not exist WHEN now playing THEN resource error`() =
        runTest {
            coEvery { serverDataSource.nowPlaying(1) } throws NetworkErrorException("")
            coEvery { localDataSource.nowPlaying() } throws Exception("")

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
            coEvery { localDataSource.search("") } throws Exception("")

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

            coEvery { localDataSource.search("") } returns arrayListOf(
                MovieDAO(
                    originalTitle = "originalTitle",
                    overview = "overview",
                    title = "title",
                    id = 1,
                    posterPath = "posterPath",
                    voteAverage = 1.0
                )
            )

            SUT.search("").test {
                assertTrue(awaitItem() is Resource.Loading)
                onLatestItem {
                    assertEquals(
                        it.data ?: emptyList(), listOf(
                            Movie(
                                title = "title",
                                overview = "overview",
                                id = 1,
                                posterPath = "https://image.tmdb.org/t/p/w342posterPath",
                                voteAverage = 1.0
                            )
                        )
                    )
                }
            }
        }

    @Test
    fun `WHEN favouriteAction THEN update local element`() =
        runTest {

            val info = MovieDescriptionDAO(
                originalTitle = "originalTitle",
                overview = "overview",
                title = "title",
                id = 1,
                posterPath = "posterPath",
                isFavourite = true,
                popularity = 1.0,
                status = "",
                tagline = "",
                voteAverage = 1.0
            )
            coEvery { localDataSource.getMovieDescriptionById(1) } returns
                    info.copy(isFavourite = false)

            SUT.favouriteAction(1, false)
            coVerify { localDataSource.saveMovieDescription(info.copy(isFavourite = false)) }
        }

    @Test
    fun `GIVEN local element does exist WHEN getMovieById THEN requests THEN return movie list`() =
        runTest {

            val info = MovieDescriptionDAO(
                originalTitle = "originalTitle",
                overview = "overview",
                title = "title",
                id = 1,
                posterPath = "posterPath",
                isFavourite = true,
                popularity = 1.0,
                status = "",
                tagline = "",
                voteAverage = 1.0
            )
            coEvery { localDataSource.getMovieDescriptionById(1) } returns info

            val result = SUT.getMovieById(1)

            assertEquals(
                result.data?.id ?: -1,
                1
            )
            assertEquals(
                result.data?.originalTitle.orEmpty(),
                "originalTitle"
            )

            assertEquals(
                result.data?.overview.orEmpty(),
                "overview"
            )
            assertEquals(
                result.data?.title.orEmpty(),
                "title"
            )
            assertEquals(
                result.data?.posterPath.orEmpty(),
                "https://image.tmdb.org/t/p/w342posterPath"
            )
            assertEquals(
                result.data?.voteAverage ?: -1.0,
                1.0
            )
            assertTrue(result.data?.isFavourite ?: false)
        }

}