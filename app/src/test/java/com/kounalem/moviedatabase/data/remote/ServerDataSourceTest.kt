package com.kounalem.moviedatabase.data.remote

import app.cash.turbine.test
import com.kounalem.moviedatabase.data.remote.models.MovieDTO
import com.kounalem.moviedatabase.data.remote.models.MovieDescriptionDTO
import com.kounalem.moviedatabase.data.remote.models.PopularMoviesDTO
import com.kounalem.moviedatabase.domain.models.Movie
import com.kounalem.moviedatabase.domain.models.MovieDescription
import com.kounalem.moviedatabase.domain.models.PopularMovies
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

internal class ServerDataSourceTest {

    @MockK
    private lateinit var service: MoviesApiService

    private val datasource by lazy {
        ServerDataSource(
            service = service,
        )
    }

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)
    }

    @Test
    fun `WHEN get movie by id THEN return DAO`() = runTest {
        coEvery { service.getMovieById(1) } returns MovieDescriptionDTO(
            id = 1,
            original_title = "original_title",
            overview = "overview",
            popularity = 123.0,
            poster_path = "poster_path",
            status = "status",
            tagline = "tagline",
            title = "title",
            vote_average = 0.0,
        )
        val expected = MovieDescription(
            id = 1,
            originalTitle = "original_title",
            overview = "overview",
            posterPath = "https://image.tmdb.org/t/p/w342poster_path",
            title = "title",
            voteAverage = 0.0,
            isFavourite = false
        )

        datasource.getMovieById(1).test {
            assertEquals(expected, awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `WHEN now playing by page id THEN return DAO`() = runTest {
        coEvery { service.nowPlaying(1) } returns PopularMoviesDTO(
            page = 1,
            movies = listOf(
                MovieDTO(
                    id = 123,
                    originalTitle = "originalTitle",
                    overview = "overview",
                    poster_path = "poster_path",
                    title = "title",
                    voteAverage = 1.0,
                    date = "2024-03-27",
                )
            ),
            totalPages = 1111111,
            totalResults = 5
        )
        val expected = PopularMovies(
            page = 1,
            movies = listOf(
                Movie(
                    id = 123,
                    posterPath = "https://image.tmdb.org/t/p/w342poster_path",
                    title = "title",
                    voteAverage = 1.0,
                    overview = "overview",
                    date = 1711497600000L,
                )
            ),
            totalPages = 1111111,
            totalResults = 5
        )

        val details = datasource.nowPlaying(1)
        assertEquals(details, expected)
    }

}