package com.kounalem.moviedatabase.tvshow.domain

import app.cash.turbine.test
import com.kounalem.moviedatabase.domain.models.TvShow
import com.kounalem.moviedatabase.repository.TvShowRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import kotlin.test.Test

class FilterShowsUCTest {
    @MockK
    private lateinit var repo: TvShowRepository

    private val usecase by lazy {
        FilterShowsUC(repo)
    }

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)
    }

    @Test
    fun `WHEN invoke THEN search the repo`() =
        runTest {
            val given = listOf(mockk<TvShow>())
            coEvery { repo.search("query") } returns flowOf(given)

            usecase.invoke("query").test {
                kotlin.test.assertEquals(given, awaitItem())
                awaitComplete()
            }
        }
}
