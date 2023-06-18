package com.kounalem.moviedatabase.data.db

import com.kounalem.moviedatabase.data.db.mapper.DescriptionDataMapper
import com.kounalem.moviedatabase.data.db.mapper.MovieMapper
import com.kounalem.moviedatabase.data.db.mapper.PopularMoviesMapper
import com.kounalem.moviedatabase.data.db.models.RoomMovie
import com.kounalem.moviedatabase.data.db.models.RoomMovieDescription
import com.kounalem.moviedatabase.data.db.models.RoomPopularMovies
import com.kounalem.moviedatabase.domain.models.Movie
import com.kounalem.moviedatabase.domain.models.MovieDescription
import com.kounalem.moviedatabase.domain.models.PopularMovies
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

internal class LocalDataSourceTest {

    @MockK
    private lateinit var dao: MovieDao

    @MockK
    private lateinit var movieMapper: MovieMapper

    @MockK
    private lateinit var descriptionDataMapper: DescriptionDataMapper

    @MockK
    private lateinit var popularMoviesMapper: PopularMoviesMapper

    private val SUT by lazy {
        LocalDataSource(
            dao = dao,
            movieMapper = movieMapper,
            descriptionDataMapper = descriptionDataMapper,
            popularMoviesMapper = popularMoviesMapper
        )
    }

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)
    }

    @Test
    fun `WHEN nowPlaying THEN return popular movie list`() = runTest {
        val local = mockk<RoomPopularMovies>()
        val popularMovies = mockk<PopularMovies>()
        every { popularMoviesMapper.mapToDomain(local) } returns popularMovies
        coEvery { dao.nowPlaying() } returns listOf(local)

        assertEquals(SUT.nowPlaying(), listOf(popularMovies))
    }

    @Test
    fun `GIVEN query WHEN getFilteredMovies THEN return movie list`() = runTest {
        val local = mockk<RoomMovie>()
        val movie = mockk<Movie>()
        val given = "query"
        every { movieMapper.mapToDomain(local) } returns movie
        coEvery { dao.getFilteredMovies(given) } returns listOf(local)

        assertEquals(SUT.getFilteredMovies(given), listOf(movie))
    }

    @Test
    fun `GIVEN page number WHEN nowPlaying THEN return popular movies info`() = runTest {
        val local = mockk<RoomPopularMovies>()
        val popularMovies = mockk<PopularMovies>()
        every { popularMoviesMapper.mapToDomain(local) } returns popularMovies
        coEvery { dao.nowPlaying(1) } returns local

        assertEquals(SUT.nowPlaying(1), popularMovies)
    }

    @Test
    fun `GIVEN id WHEN getMovieDescriptionById THEN return movie details`() = runTest {
        val local = mockk<RoomMovieDescription>()
        val description = mockk<MovieDescription>()
        every { descriptionDataMapper.mapToDomain(local) } returns description
        coEvery { dao.getMovieDescriptionById(1) } returns local

        assertEquals(SUT.getMovieDescriptionById(1), description)
    }

    @Test
    fun `GIVEN popular movies WHEN save movie THEN save each movie in particular`() = runTest {
        val local = mockk<RoomPopularMovies>()
        val movie = mockk<Movie>()
        val given = mockk<PopularMovies>() {
            every { movies } returns listOf(movie, movie)
        }
        every { popularMoviesMapper.mapToRoom(given) } returns local
        coEvery { dao.saveMovie(local) } returns Unit
        coEvery { dao.saveMovie(any<RoomMovie>()) } returns Unit
        every { movieMapper.mapToRoom(any<Movie>()) } returns mockk<RoomMovie>()

        SUT.saveMovie(given)

        coVerify { dao.saveMovie(any<RoomMovie>()) }
    }

    @Test
    fun `WHEN saveMovieDescription THEN call dao`() = runTest {
        val local = mockk<RoomMovieDescription>()
        val given = mockk<MovieDescription>()

        every { descriptionDataMapper.mapToRoom(given) } returns local
        coEvery { dao.saveMovieDescription(local) } returns Unit

        SUT.saveMovieDescription(given)

        coVerify { dao.saveMovieDescription(local) }
    }
}