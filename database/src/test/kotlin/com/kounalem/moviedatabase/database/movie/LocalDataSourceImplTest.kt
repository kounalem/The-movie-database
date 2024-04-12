package com.kounalem.moviedatabase.database.movie

import app.cash.turbine.test
import com.kounalem.moviedatabase.database.movie.models.MovieDescriptionEntity
import com.kounalem.moviedatabase.database.movie.models.MovieEntity
import com.kounalem.moviedatabase.database.movie.models.TvShowEntity
import com.kounalem.moviedatabase.domain.models.Movie
import com.kounalem.moviedatabase.domain.models.MovieDescription
import com.kounalem.moviedatabase.domain.models.TvShow
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

internal class LocalDataSourceImplTest {

    @MockK
    private lateinit var movieDao: MovieDao

    @MockK
    private lateinit var showDao: TvShowsDao

    private val dataSource by lazy {
        LocalDataSourceImpl(daoMovies = movieDao, daoTvShows = showDao)
    }
    private val dummyTvShow by lazy {
        TvShow(
            id = 1,
            overview = "overview",
            posterPath = null,
            name = "title",
            voteAverage = null,
            adult = false,
            originCountry = listOf(),
            originalLanguage = "lobortis",
            originalName = "Darnell Baxter",
            popularity = 12.13,
            firstAirDate = "parturient",
            voteCount = 9391,
            languages = listOf(),
            lastAirDate = null,
            seasons = listOf(),
            type = null,
            isFavourite = false
        )
    }

    private val dummyTvShowEntity by lazy {
        TvShowEntity(
            id = 1,
            overview = "overview",
            posterPath = null,
            title = "title",
            voteAverage = null,
            adult = false,
            originCountry = listOf(),
            originalLanguage = "lobortis",
            originalName = "Darnell Baxter",
            popularity = 12.13,
            firstAirDate = "parturient",
            voteCount = 9391,
            languages = listOf(),
            lastAirDate = null,
            seasons = listOf(),
            type = null,
            isFavourite = false
        )
    }
    private val dummyMovieDescriptionEntity by lazy {
        MovieDescriptionEntity(
            id = 1,
            originalTitle = "original_title",
            overview = "overview",
            posterPath = "https://image.tmdb.org/t/p/w342poster_path",
            title = "title",
            voteAverage = 0.0,
            isFavourite = false
        )
    }
    private val dummyMovieDescription by lazy {
        MovieDescription(
            id = 1,
            originalTitle = "original_title",
            overview = "overview",
            posterPath = "https://image.tmdb.org/t/p/w342poster_path",
            title = "title",
            voteAverage = 0.0,
            isFavourite = false
        )
    }

    private val dummyMovie by lazy {
        Movie(
            id = 1,
            posterPath = "",
            title = "",
            voteAverage = 0.0,
            overview = "",
            date = 123
        )
    }
    private val dummyMovieEntity by lazy {
        MovieEntity(
            id = 1,
            overview = "",
            posterPath = "",
            title = "",
            voteAverage = 0.0,
            date = 123,
        )
    }

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)
    }

    @Test
    fun `GIVEN query WHEN getFilteredMovies THEN return filtered domain movie list models`() =
        runTest {
            val given = "query"
            coEvery { movieDao.getFilteredMovies(given) } returns flowOf(listOf(dummyMovieEntity))

            dataSource.getFilteredMovies(given).test {
                assertEquals(
                    listOf(
                        dummyMovie
                    ), awaitItem()
                )
                awaitComplete()
            }
        }

    @Test
    fun `WHEN get all movies THEN return mapped domain model`() = runTest {

        every { movieDao.getAllMovies() } returns flowOf(listOf(dummyMovieEntity))

        dataSource.getAllMovies().test {
            assertEquals(listOf(dummyMovie), awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `give movie list WHEN save movie list THEN save mapped to entity model`() = runTest {
        dataSource.saveMovieList(listOf(dummyMovie))

        coVerify { movieDao.saveMovie(dummyMovieEntity) }
    }

    @Test
    fun `WHEN saveMovieDescription THEN call dao`() = runTest {
        dataSource.saveMovieDescription(
            dummyMovieDescription
        )

        coVerify { movieDao.saveMovieDescription(dummyMovieDescriptionEntity) }
    }

    @Test
    fun `GIVEN query WHEN getFilteredShows THEN return filtered domain show list models`() =
        runTest {
            val given = "query"
            coEvery { showDao.getFilteredShows(given) } returns flowOf(listOf(dummyTvShowEntity))

            dataSource.getFilteredShows(given).test {
                assertEquals(listOf(dummyTvShow), awaitItem())
                awaitComplete()
            }
        }

    @Test
    fun `WHEN get all shows THEN return mapped domain model`() = runTest {
        every { showDao.getAllShows() } returns flowOf(listOf(dummyTvShowEntity))

        dataSource.getAllShows().test {
            assertEquals(listOf(dummyTvShow), awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `give tv show list WHEN save show list THEN save mapped to entity model`() = runTest {
        dataSource.saveShowList(listOf(dummyTvShow))

        coVerify { showDao.saveShow(dummyTvShowEntity) }
    }

    @Test
    fun `WHEN saveShowDescription THEN call dao`() = runTest {
        dataSource.saveShowDescription(dummyTvShow)

        coVerify { showDao.saveShow(dummyTvShowEntity) }
    }
}