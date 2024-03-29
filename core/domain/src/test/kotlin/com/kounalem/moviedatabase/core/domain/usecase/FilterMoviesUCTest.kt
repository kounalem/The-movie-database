package com.kounalem.moviedatabase.core.domain.usecase

import app.cash.turbine.test
import com.kounalem.moviedatabase.core.data.movie.MovieRepository
import com.kounalem.moviedatabase.domain.models.Movie
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import kotlin.test.Test
import kotlin.test.assertEquals

class FilterMoviesUCTest {
    @MockK
    private lateinit var repo: MovieRepository

    private val usecase by lazy {
        FilterMoviesUC(repo)
    }

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)
    }

    @Test
    fun `WHEN invoke THEN search the repo`() = runTest {
        val given = listOf(mockk<Movie>())
        coEvery { repo.search("query") } returns flowOf(given)

        usecase.invoke("query").test {
            assertEquals(given, awaitItem())
            awaitComplete()
        }
    }
}