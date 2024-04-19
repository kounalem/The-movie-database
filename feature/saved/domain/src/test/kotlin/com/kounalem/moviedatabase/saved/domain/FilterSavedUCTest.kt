package com.kounalem.moviedatabase.saved.domain

import com.kounalem.moviedatabase.domain.models.TvShow
import com.kounalem.moviedatabase.repository.MovieRepository
import com.kounalem.moviedatabase.repository.TvShowRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import kotlin.test.Test
import kotlin.test.assertEquals

class FilterSavedUCTest {
    @MockK
    private lateinit var movieRepo: MovieRepository

    @MockK
    private lateinit var showRepo: TvShowRepository

    private val usecase by lazy {
        FilterSavedUC(movieRepo, showRepo)
    }

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)
    }

    @Test
    fun `WHEN invoke THEN return saved movies and shows`() = runTest {
        coEvery { showRepo.getAllLocalSavedShows() } returns flowOf(
            listOf(
                TvShow(
                    adult = false,
                    id = 1336,
                    originalName = "Woodrow Dickson",
                    overview = "mnesarchum",
                    posterPath = "praesent",
                    firstAirDate = "moderatius",
                    name = "Marianne Lowery",
                    languages = listOf(),
                    lastAirDate = null,
                    seasons = listOf(),
                    type = null,
                    isFavourite = true
                ),
                TvShow(
                    adult = false,
                    id = 5459,
                    originalName = "Autumn Buckley",
                    overview = "scripta",
                    posterPath = "utroque",
                    firstAirDate = "verterem",
                    name = "Tanya Ferrell",
                    languages = listOf(),
                    lastAirDate = null,
                    seasons = listOf(),
                    type = null,
                    isFavourite = false
                )
            )
        )
        coEvery { showRepo.getAllLocalSavedShows() } returns flowOf(
            listOf(
                TvShow(
                    adult = false,
                    id = 4048,
                    originalName = "Rod Oliver",
                    overview = "iuvaret",
                    posterPath = "eius",
                    firstAirDate = "pharetra",
                    name = "Trenton Levine",
                    languages = listOf(),
                    lastAirDate = null,
                    seasons = listOf(),
                    type = null,
                    isFavourite = false
                ),
                TvShow(
                    adult = false,
                    id = 3115,
                    originalName = "Mickey Rutledge",
                    overview = "splendide",
                    posterPath = "oporteat",
                    firstAirDate = "erroribus",
                    name = "Rickey Abbott",
                    languages = listOf(),
                    lastAirDate = null,
                    seasons = listOf(),
                    type = null,
                    isFavourite = true
                )
            )
        )
        usecase.invoke().collect { savedElements ->
            assertEquals(
                listOf(
                    FilterSavedUC.SavedElement.Movie(
                        1336, "praesent"
                    ),
                    FilterSavedUC.SavedElement.Movie(
                        3115, "oporteat"
                    )
                ),
                savedElements
            )
        }
    }
}