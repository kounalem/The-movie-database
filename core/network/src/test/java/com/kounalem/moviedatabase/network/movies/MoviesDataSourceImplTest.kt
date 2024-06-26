package com.kounalem.moviedatabase.network.movies

import app.cash.turbine.test
import com.kounalem.moviedatabase.domain.models.Movie
import com.kounalem.moviedatabase.domain.models.PopularMovies
import com.kounalem.moviedatabase.network.NetworkResponse
import com.kounalem.moviedatabase.network.dtoToResponse
import com.kounalem.moviedatabase.network.movies.models.MovieDTO
import com.kounalem.moviedatabase.network.movies.models.PopularMoviesDTO
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

internal class MoviesDataSourceImplTest {
    @MockK
    private lateinit var service: MoviesApiService

    private val datasource by lazy {
        ServerDataSourceImpl(service = service)
    }

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)
    }

    @Test
    fun `WHEN now playing by page id THEN return DAO`() =
        runTest {
            coEvery { service.nowPlaying(1) } returns
                PopularMoviesDTO(
                    page = 1,
                    movies =
                        listOf(
                            MovieDTO(
                                id = 123,
                                originalTitle = "originalTitle",
                                overview = "overview",
                                posterPath = "poster_path",
                                title = "title",
                                voteAverage = 1.0,
                                date = "2024-03-27",
                            ),
                        ),
                    totalPages = 1111111,
                    totalResults = 5,
                ).dtoToResponse()
            val expected =
                NetworkResponse.Success(
                    PopularMovies(
                        page = 1,
                        movies =
                            listOf(
                                Movie(
                                    id = 123,
                                    posterPath = "https://image.tmdb.org/t/p/w342poster_path",
                                    title = "title",
                                    voteAverage = 1.0,
                                    overview = "overview",
                                    date = 1711497600000L,
                                    page = 1,
                                    isFavourite = false,
                                    originalTitle = "originalTitle",
                                ),
                            ),
                        totalPages = 1111111,
                        totalResults = 5,
                    ),
                )

            datasource.nowPlaying(1).test {
                assertEquals(expected, awaitItem())
                awaitComplete()
            }
        }
}
