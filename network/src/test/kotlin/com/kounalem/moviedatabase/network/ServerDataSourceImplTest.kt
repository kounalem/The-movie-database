package com.kounalem.moviedatabase.network

import app.cash.turbine.test
import com.kounalem.moviedatabase.domain.models.Movie
import com.kounalem.moviedatabase.domain.models.MovieDescription
import com.kounalem.moviedatabase.domain.models.PopularMovies
import com.kounalem.moviedatabase.network.movies.models.MovieDTO
import com.kounalem.moviedatabase.network.movies.models.MovieDescriptionDTO
import com.kounalem.moviedatabase.network.movies.models.PopularMoviesDTO
import com.kounalem.moviedatabase.network.movies.MoviesApiService
import com.kounalem.moviedatabase.network.movies.ServerDataSourceImpl
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Response

internal class ServerDataSourceImplTest {

    @MockK
    private lateinit var service: MoviesApiService

    private val datasource by lazy {
        ServerDataSourceImpl(
            service = service,
        )
    }

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)
    }

    private fun <T> T.dtoToResponse() = mockk<Response<T>> {
        every { isSuccessful } returns true
        every { body() } returns this@dtoToResponse
    }

    @Test
    fun `WHEN get movie by id THEN return DAO`() = runTest {
        coEvery { service.getMovieById(1) } returns MovieDescriptionDTO(
            id = 1,
            originalTitle = "original_title",
            overview = "overview",
            popularity = 123.0,
            posterPath = "poster_path",
            status = "status",
            tagline = "tagline",
            title = "title",
            voteAverage = 0.0,
        ).dtoToResponse()
        val expected = NetworkResponse.Success(
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
                    posterPath = "poster_path",
                    title = "title",
                    voteAverage = 1.0,
                    date = "2024-03-27",
                )
            ),
            totalPages = 1111111,
            totalResults = 5
        ).dtoToResponse()
        val expected = NetworkResponse.Success(
            PopularMovies(
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
        )

        datasource.nowPlaying(1).test {
            assertEquals(expected, awaitItem())
            awaitComplete()
        }
    }

}