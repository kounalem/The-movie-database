package com.kounalem.moviedatabaase.data

import android.accounts.NetworkErrorException
import app.cash.turbine.test
import com.kounalem.moviedatabaase.CoroutineTestRule
import com.kounalem.moviedatabaase.data.db.LocalDataSource
import com.kounalem.moviedatabaase.data.db.models.RoomMovie
import com.kounalem.moviedatabaase.data.db.models.RoomMovieDescription
import com.kounalem.moviedatabaase.data.db.models.RoomPopularMovies
import com.kounalem.moviedatabaase.data.mappers.MovieDataMapper
import com.kounalem.moviedatabaase.data.mappers.MovieDescriptionDataMapper
import com.kounalem.moviedatabaase.data.mappers.PopularMoviesDataMapper
import com.kounalem.moviedatabaase.data.remote.ServerDataSource
import com.kounalem.moviedatabaase.data.remote.models.MovieDTO
import com.kounalem.moviedatabaase.data.remote.models.PopularMoviesDTO
import com.kounalem.moviedatabaase.domain.models.Movie
import com.kounalem.moviedatabaase.domain.models.MovieDescription
import com.kounalem.moviedatabaase.domain.models.PopularMovies
import com.kounalem.moviedatabaase.util.Resource
import com.kounalem.moviedatabaase.utils.onLatestItem
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
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
            totalPages = 5,
            totalResults = 20,
        )
        val localMovies = RoomPopularMovies(
            page = 0,
            id = 1,
            totalPages = 5,
            totalResults = 20,
            movies = arrayListOf(
                RoomMovie(
                    originalTitle = "originalTitle",
                    overview = "overview",
                    title = "title",
                    id = 1,
                    posterPath = "posterPath",
                    voteAverage = 1.0
                )
            )
        )
        val popularMovies = PopularMovies(
            id = 1,
            page = 0,
            totalPages = 5,
            totalResults = 20,
            movies = listOf(
                Movie(
                    id = 1,
                    posterPath = "posterPath",
                    title = "title",
                    voteAverage = 1.0,
                    overview = "overview"
                )
            )
        )
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
            val localMovies =
                RoomPopularMovies(
                    page = 0,
                    id = 1,
                    totalPages = 5,
                    totalResults = 20,
                    movies = arrayListOf(
                        RoomMovie(
                            originalTitle = "originalTitle",
                            overview = "overview",
                            title = "title",
                            id = 1,
                            posterPath = "posterPath",
                            voteAverage = 1.0
                        )
                    )
                )
            val popularMovies = PopularMovies(
                id = 1,
                page = 0,
                totalPages = 5,
                totalResults = 20,
                movies = listOf(
                    Movie(
                        id = 1,
                        posterPath = "posterPath",
                        title = "title",
                        voteAverage = 1.0,
                        overview = "overview"
                    )
                )
            )
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
            val roomMovie = RoomMovie(
                originalTitle = "originalTitle",
                overview = "overview",
                title = "title",
                id = 1,
                posterPath = "posterPath",
                voteAverage = 1.0
            )
            val localMovies =
                RoomPopularMovies(
                    page = 0,
                    id = 1,
                    totalPages = 5,
                    totalResults = 20,
                    movies = arrayListOf(
                        roomMovie
                    )
                )
            val movie = Movie(
                id = 1,
                posterPath = "posterPath",
                title = "title",
                voteAverage = 1.0,
                overview = "overview"
            )
            val popularMovies = PopularMovies(
                id = 1,
                page = 0,
                totalPages = 5,
                totalResults = 20,
                movies = listOf(
                    movie
                )
            )
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
            coEvery { localDataSource.getMovieDescriptionById(1) } returns info
            val movieDescription = MovieDescription(
                originalTitle = "originalTitle",
                overview = "overview",
                title = "title",
                id = 1,
                posterPath = "posterPath",
                isFavourite = true,
                voteAverage = 1.0,
            )
            every { movieDescriptionDataMapper.map(info) } returns movieDescription
            val result = SUT.getMovieById(1)

            assertEquals(
                result.data,
                movieDescription
            )
        }

}