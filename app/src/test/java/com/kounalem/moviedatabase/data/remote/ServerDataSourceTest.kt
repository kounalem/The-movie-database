package com.kounalem.moviedatabase.data.remote

import com.kounalem.moviedatabase.data.remote.mapper.mappers.MovieDescriptionMapper
import com.kounalem.moviedatabase.data.remote.mapper.mappers.PopularMoviesMapper
import com.kounalem.moviedatabase.data.remote.models.MovieDescriptionDTO
import com.kounalem.moviedatabase.data.remote.models.PopularMoviesDTO
import com.kounalem.moviedatabase.domain.models.MovieDescription
import com.kounalem.moviedatabase.domain.models.PopularMovies
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals


internal class ServerDataSourceTest {

    @MockK
    private lateinit var service: MoviesApiService

    @MockK
    private lateinit var popularMoviesMapper: PopularMoviesMapper

    @MockK
    private lateinit var descriptionMapper: MovieDescriptionMapper
    private val SUT by lazy {
        ServerDataSource(
            service = service,
            descriptionMapper = descriptionMapper,
            popularMoviesMapper = popularMoviesMapper,
        )
    }

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)
    }

    @Test
    fun `WHEN get movie by id THEN return DAO`() = runTest {
        val dto = mockk<MovieDescriptionDTO>()
        coEvery { service.getMovieById(1) } returns dto
        val domain = mockk<MovieDescription>()
        every { descriptionMapper.map(dto) } returns domain

        val details = SUT.getMovieById(1)
        assertEquals(details, domain)
    }

    @Test
    fun `WHEN now playing by page id THEN return DAO`() = runTest {
        val dto = mockk<PopularMoviesDTO>()
        coEvery { service.nowPlaying(1) } returns dto
        val domain = mockk<PopularMovies>()
        every { popularMoviesMapper.map(dto) } returns domain

        val details = SUT.nowPlaying(1)
        assertEquals(details, domain)
    }

}