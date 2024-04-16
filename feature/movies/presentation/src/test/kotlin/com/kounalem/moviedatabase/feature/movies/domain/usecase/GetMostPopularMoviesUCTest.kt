package com.kounalem.moviedatabase.feature.movies.domain.usecase

import app.cash.turbine.test
import com.kounalem.moviedatabase.domain.models.Movie
import com.kounalem.moviedatabase.repository.MovieRepository
import com.kounalem.moviedatabase.repository.Outcome
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals


class GetMostPopularMoviesUCTest {
    @MockK
    private lateinit var movieRepository: MovieRepository
    private val usecase by lazy {
        GetMostPopularMoviesUC(movieRepository)
    }

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)
    }

    @Test
    fun `WHEN movies has error outcome THEN usecase returns throwable`() = runTest {
        coEvery { movieRepository.movies } returns flowOf(Outcome.Error("my error"))

        usecase.movies.catch {
            assertEquals(Throwable("my error"), it)
        }
    }

    @Test
    fun `WHEN movies has Exception outcome THEN usecase returns throwable`() = runTest {
        coEvery { movieRepository.movies } returns flowOf(Outcome.Exception(Throwable("my error")))

        usecase.movies.catch {
            assertEquals(Throwable("my error"), it)
        }
    }

    @Test
    fun `WHEN movies has Success outcome THEN usecase returns throwable`() = runTest {
        val given = listOf(
            Movie(
                id = 1,
                posterPath = "",
                title = "title1",
                voteAverage = 2.0,
                overview = "overview1",
                date = 123,
                isFavourite = false,
                originalTitle = "",
                page = 1
            ), Movie(
                id = 2,
                posterPath = "",
                title = "title2",
                voteAverage = 2.0,
                overview = "overview2",
                date = 123,
                isFavourite = false,
                originalTitle = "",
                page = 1
            )
        )
        coEvery { movieRepository.movies } returns flowOf(
            Outcome.Success(
                given
            )
        )
        val expected = LinkedHashMap<Int, Movie>().apply {
            given.forEach { put(it.id, it) }
        }
        usecase.movies.test {
            assertEquals(expected, awaitItem())
        }
    }
}