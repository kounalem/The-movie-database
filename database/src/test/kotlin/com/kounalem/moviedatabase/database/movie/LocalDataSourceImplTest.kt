package com.kounalem.moviedatabase.database.movie

import app.cash.turbine.test
import com.kounalem.moviedatabase.database.movie.models.MovieDescriptionEntity
import com.kounalem.moviedatabase.database.movie.models.MovieEntity
import com.kounalem.moviedatabase.domain.models.Movie
import com.kounalem.moviedatabase.domain.models.MovieDescription
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
    private lateinit var dao: MovieDao


    private val dataSource by lazy {
        LocalDataSourceImpl(dao = dao)
    }

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)
    }

    @Test
    fun `GIVEN query WHEN getFilteredMovies THEN return filtered domain movie list models`() =
        runTest {
            val local = MovieEntity(
                id = 1,
                overview = null,
                posterPath = null,
                title = null,
                voteAverage = null,
                date = 123,
            )
            val given = "query"
            coEvery { dao.getFilteredMovies(given) } returns flowOf(listOf(local))

            dataSource.getFilteredMovies(given).test {
                assertEquals(
                    listOf(
                        Movie(
                            id = 1,
                            posterPath = "",
                            title = "",
                            voteAverage = 0.0,
                            overview = "",
                            date = 123
                        )
                    ), awaitItem()
                )
                awaitComplete()
            }
        }

    @Test
    fun `WHEN get all movies THEN return mapped domain model`() = runTest {
        val given = MovieEntity(
            id = 1,
            overview = null,
            posterPath = null,
            title = null,
            voteAverage = null,
            date = 123,
        )
        val expected = Movie(
            id = 1,
            posterPath = "",
            title = "",
            voteAverage = 0.0,
            overview = "",
            date = 123
        )
        every { dao.getAllMovies() } returns flowOf(listOf(given))

        dataSource.getAllMovies().test {
            assertEquals(listOf(expected), awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `give movie list WHEN save movie list THEN save mapped to entity model`() = runTest {
        dataSource.saveMovieList(
            listOf(
                Movie(
                    id = 1,
                    posterPath = "",
                    title = "",
                    voteAverage = 0.0,
                    overview = "",
                    date = 123
                )
            )
        )

        coVerify {
            dao.saveMovie(
                MovieEntity(
                    id = 1,
                    overview = "",
                    posterPath = "",
                    title = "",
                    voteAverage = 0.0,
                    date = 123,
                )
            )
        }
    }

    @Test
    fun `WHEN saveMovieDescription THEN call dao`() = runTest {
        dataSource.saveMovieDescription(
            MovieDescription(
                id = 1,
                originalTitle = "original_title",
                overview = "overview",
                posterPath = "https://image.tmdb.org/t/p/w342poster_path",
                title = "title",
                voteAverage = 0.0,
                isFavourite = false
            )
        )

        coVerify {
            dao.saveMovieDescription(
                MovieDescriptionEntity(
                    id = 1,
                    originalTitle = "original_title",
                    overview = "overview",
                    posterPath = "https://image.tmdb.org/t/p/w342poster_path",
                    title = "title",
                    voteAverage = 0.0,
                    isFavourite = false
                )
            )
        }
    }
}