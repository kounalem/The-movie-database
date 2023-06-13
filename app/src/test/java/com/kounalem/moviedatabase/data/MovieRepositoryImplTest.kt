package com.kounalem.moviedatabase.data

import android.accounts.NetworkErrorException
import app.cash.turbine.test
import com.kounalem.moviedatabase.CoroutineTestRule
import com.kounalem.moviedatabase.data.db.LocalDataSource
import com.kounalem.moviedatabase.data.db.models.RoomMovie
import com.kounalem.moviedatabase.data.db.models.RoomMovieDescription
import com.kounalem.moviedatabase.data.db.models.RoomPopularMovies
import com.kounalem.moviedatabase.data.mappers.MovieDataMapper
import com.kounalem.moviedatabase.data.mappers.MovieDescriptionDataMapper
import com.kounalem.moviedatabase.data.mappers.PopularMoviesDataMapper
import com.kounalem.moviedatabase.data.remote.ServerDataSource
import com.kounalem.moviedatabase.data.remote.models.PopularMoviesDTO
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

    @MockK
    private lateinit var movieDescriptionDataMapper: MovieDescriptionDataMapper

    @MockK
    private lateinit var movieDataMapper: MovieDataMapper

    @MockK
    private lateinit var popularMoviesDataMapper: PopularMoviesDataMapper
    private val SUT by lazy {
        MovieRepositoryImpl(
            serverDataSource = serverDataSource,
            localDataSource = localDataSource,
            movieDescriptionDataMapper = movieDescriptionDataMapper,
            movieDataMapper = movieDataMapper,
            popularMoviesDataMapper = popularMoviesDataMapper
        )
    }

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)
    }

    @Test
    fun `WHEN now playing THEN requests then get network favourite movie`() = runTest {
        val dao = mockk<PopularMoviesDTO>()
        val localMovies = mockk<RoomPopularMovies>()
        val popularMovies = mockk<PopularMovies>()
        every { popularMoviesDataMapper.map(dao) } returns localMovies
        coEvery { serverDataSource.nowPlaying(1) } returns dao
        coEvery { localDataSource.saveMovie(any<RoomPopularMovies>()) } returns Unit
        every { popularMoviesDataMapper.map(localMovies) } returns popularMovies

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
            val localMovies = mockk<RoomPopularMovies>()
            val popularMovies = mockk<PopularMovies>()
            every { popularMoviesDataMapper.map(localMovies) } returns popularMovies

            coEvery { serverDataSource.nowPlaying(1) } throws NetworkErrorException("")
            coEvery { localDataSource.nowPlaying() } returns listOf(localMovies)
            coEvery { localDataSource.saveMovie(any<RoomMovie>()) } returns Unit
            coEvery { localDataSource.saveMovie(any<RoomPopularMovies>()) } returns Unit

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
            coEvery { localDataSource.nowPlaying() } returns emptyList()

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
            coEvery { localDataSource.nowPlaying() } throws Exception("")

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
            val roomMovie = mockk<RoomMovie>()
            val localMovies = mockk<RoomPopularMovies> {
                every { movies } returns arrayListOf(roomMovie)
            }
            val movie = mockk<Movie>(relaxed = true)
            val popularMovies = mockk<PopularMovies> {
                every { movies } returns listOf(movie)
            }
            every { popularMoviesDataMapper.map(localMovies) } returns popularMovies
            coEvery { localDataSource.nowPlaying() } returns listOf(localMovies)
            every { movieDataMapper.map(roomMovie) } returns movie

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

            val info = RoomMovieDescription(
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

            val info = mockk<RoomMovieDescription> {
                every { id } returns 1
            }
            coEvery { localDataSource.getMovieDescriptionById(1) } returns info
            val movieDescription = mockk<MovieDescription>() {
                every { id } returns 1
            }
            every { movieDescriptionDataMapper.map(info) } returns movieDescription
            val result = SUT.getMovieById(1)

            assertEquals(
                result.data,
                movieDescription
            )
        }

}