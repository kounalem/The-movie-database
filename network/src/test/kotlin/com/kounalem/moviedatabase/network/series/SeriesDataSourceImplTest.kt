package com.kounalem.moviedatabase.network.series

import app.cash.turbine.test
import com.google.gson.Gson
import com.kounalem.moviedatabase.domain.models.TvShow
import com.kounalem.moviedatabase.network.NetworkResponse
import com.kounalem.moviedatabase.network.dtoToResponse
import com.kounalem.moviedatabase.network.series.models.TvShowDetailsResponseDTO
import com.kounalem.moviedatabase.network.series.models.TvShowResponseDTO
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class SeriesDataSourceImplTest {
    @MockK
    private lateinit var service: SeriesApiService

    private val datasource by lazy {
        SeriesDataSourceImpl(service = service)
    }

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)
    }

    @Test
    fun `WHEN get series by id THEN return DAO`() = runTest {
        val given = Gson().fromJson(detailsJsonString, TvShowDetailsResponseDTO::class.java)
        val response = given.dtoToResponse()
        coEvery { service.getSeriesById(1) } returns response
        val expected = NetworkResponse.Success(
            TvShow(
                adult = false,
                id = 61818,
                originalName = "Late Night with Seth Meyers",
                overview = """Seth Meyers, who is "Saturday Night Live’s" longest serving anchor on the show’s wildly popular "Weekend Update," takes over as host of NBC’s "Late Night" — home to A-list celebrity guests, memorable comedy and the best in musical talent. As the Emmy Award-winning head writer for "SNL," Meyers has established a reputation for sharp wit and perfectly timed comedy, and has gained fame for his spot-on jokes and satire. Meyers takes his departure from "SNL" to his new post at "Late Night," as Jimmy Fallon moves to "The Tonight Show".""".trimIndent(),
                posterPath = "https://image.tmdb.org/t/p/w342/g6MrJxNaHYGYU7Sxo72e5B8gKOV.jpg",
                firstAirDate = "2014-02-25",
                name = "Late Night with Seth Meyers",
                languages = listOf("en"),
                lastAirDate = "2024-04-10",
                seasons = listOf(
                    TvShow.Season(
                        airDate = "2014-02-25",
                        episodeCount = 99,
                        id = 64330,
                        name = "Season 1",
                        overview = "",
                        posterPath = "https://image.tmdb.org/t/p/w342/6aTObv741nQNeIrNhevVw3OlVQw.jpg",
                        seasonNumber = 1,
                        voteAverage = 0.0
                    ),
                ),
                type = "Talk Show",
                isFavourite = false,
            )
        )

        datasource.getSeriesById(1).test {
            assertEquals(expected, awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `WHEN now playing by page id THEN return DAO`() = runTest {
        val given = Gson().fromJson(popularJsonString, TvShowResponseDTO::class.java)
        val response = given.dtoToResponse()

        coEvery { service.popular(1) } returns response
        val expected = NetworkResponse.Success(
            listOf(
                TvShow(
                    adult = false,
                    id = 22980,
                    originalName = "Watch What Happens Live with Andy Cohen",
                    overview = "Bravo network executive Andy Cohen discusses pop culture topics with celebrities and reality show personalities.",
                    posterPath = "https://image.tmdb.org/t/p/w342/onSD9UXfJwrMXWhq7UY7hGF2S1h.jpg",
                    firstAirDate = "2009-07-16",
                    name = "Watch What Happens Live with Andy Cohen",
                    languages = null,
                    lastAirDate = null,
                    seasons = null,
                    type = null,
                    isFavourite = false,
                ),
                TvShow(
                    adult = false,
                    id = 61818,
                    originalName = "Late Night with Seth Meyers",
                    overview = "Seth Meyers, who is \"Saturday Night Live’s\" longest serving anchor on the show’s wildly popular \"Weekend Update,\" takes over as host of NBC’s \"Late Night\" — home to A-list celebrity guests, memorable comedy and the best in musical talent. As the Emmy Award-winning head writer for \"SNL,\" Meyers has established a reputation for sharp wit and perfectly timed comedy, and has gained fame for his spot-on jokes and satire. Meyers takes his departure from \"SNL\" to his new post at \"Late Night,\" as Jimmy Fallon moves to \"The Tonight Show\".",
                    posterPath = "https://image.tmdb.org/t/p/w342/g6MrJxNaHYGYU7Sxo72e5B8gKOV.jpg",
                    firstAirDate = "2014-02-25",
                    name = "Late Night with Seth Meyers",
                    languages = null,
                    lastAirDate = null,
                    seasons = null,
                    type = null,
                    isFavourite = false,
                )
            )
        )

        datasource.popular(1).test {
            assertEquals(expected, awaitItem())
            awaitComplete()
        }
    }


    private val detailsJsonString = """
    {
      "adult": false,
      "backdrop_path": "/dfX2UaHVE5c7kLBFbgmEZJuy4Ev.jpg",
      "created_by": [],
      "episode_run_time": [45],
      "first_air_date": "2014-02-25",
      "genres": [
        {
          "id": 10767,
          "name": "Talk"
        },
        {
          "id": 35,
          "name": "Comedy"
        }
      ],
      "homepage": "https://www.nbc.com/late-night-with-seth-meyers",
      "id": 61818,
      "in_production": true,
      "languages": ["en"],
      "last_air_date": "2024-04-10",
      "last_episode_to_air": {
        "id": 5258512,
        "name": "Daniel Radcliffe, Neal Brennan, George Motz",
        "overview": "Seth welcomes actor Daniel Radcliffe, comic Neal Brennan, chef George Motz, and Andrew Hurley sits in with the 8G Band.",
        "vote_average": 0,
        "vote_count": 0,
        "air_date": "2024-04-10",
        "episode_number": 88,
        "episode_type": "standard",
        "production_code": "",
        "runtime": 41,
        "season_number": 11,
        "show_id": 61818,
        "still_path": "/YthzOvsH70BwinLDFAsAM8v0Bs.jpg"
      },
      "name": "Late Night with Seth Meyers",
      "next_episode_to_air": {
        "id": 5258513,
        "name": "Kirsten Dunst, Sean Casey, Ryan Dempster",
        "overview": "Kirsten Dunst, Sean Casey, Ryan Dempster",
        "vote_average": 0,
        "vote_count": 0,
        "air_date": "2024-04-11",
        "episode_number": 89,
        "episode_type": "standard",
        "production_code": "",
        "runtime": 41,
        "season_number": 11,
        "show_id": 61818,
        "still_path": null
      },
      "networks": [
        {
          "id": 6,
          "logo_path": "/cm111bsDVlYaC1foL0itvEI4yLG.png",
          "name": "NBC",
          "origin_country": "US"
        }
      ],
      "number_of_episodes": 1512,
      "number_of_seasons": 11,
      "origin_country": ["US"],
      "original_language": "en",
      "original_name": "Late Night with Seth Meyers",
      "overview": "Seth Meyers, who is \"Saturday Night Live’s\" longest serving anchor on the show’s wildly popular \"Weekend Update,\" takes over as host of NBC’s \"Late Night\" — home to A-list celebrity guests, memorable comedy and the best in musical talent. As the Emmy Award-winning head writer for \"SNL,\" Meyers has established a reputation for sharp wit and perfectly timed comedy, and has gained fame for his spot-on jokes and satire. Meyers takes his departure from \"SNL\" to his new post at \"Late Night,\" as Jimmy Fallon moves to \"The Tonight Show\".",
      "popularity": 6994.885,
      "poster_path": "/g6MrJxNaHYGYU7Sxo72e5B8gKOV.jpg",
      "production_companies": [
        {
          "id": 26727,
          "logo_path": "/jeTxdjXhzgKZyLr3l9MllkTn3fy.png",
          "name": "Universal Television",
          "origin_country": "US"
        },
        {
          "id": 170657,
          "logo_path": null,
          "name": "Sethmaker Shoemeyers Productions",
          "origin_country": "US"
        },
        {
          "id": 1756,
          "logo_path": "/d7YyNzysIF0lSb4lJRf1ar6MHxi.png",
          "name": "Broadway Video",
          "origin_country": "US"
        }
      ],
      "production_countries": [
        {
          "iso_3166_1": "US",
          "name": "United States of America"
        }
      ],
      "seasons": [
        {
          "air_date": "2014-02-25",
          "episode_count": 99,
          "id": 64330,
          "name": "Season 1",
          "overview": "",
          "poster_path": "/6aTObv741nQNeIrNhevVw3OlVQw.jpg",
          "season_number": 1,
          "vote_average": 0
        }
      ],
      "spoken_languages": [
        {
          "english_name": "English",
          "iso_639_1": "en",
          "name": "English"
        }
      ],
      "status": "Returning Series",
      "tagline": "One man dares to take a closer look.",
      "type": "Talk Show",
      "vote_average": 5.55,
      "vote_count": 80
    }
"""

    private val popularJsonString = "{\n" +
            "  \"page\": 1,\n" +
            "  \"results\": [\n" +
            "    {\n" +
            "      \"adult\": false,\n" +
            "      \"backdrop_path\": \"/butPVWgcbtAjL9Z7jU7Xj1KA8KD.jpg\",\n" +
            "      \"genre_ids\": [\n" +
            "        10767,\n" +
            "        35\n" +
            "      ],\n" +
            "      \"id\": 22980,\n" +
            "      \"origin_country\": [\n" +
            "        \"US\"\n" +
            "      ],\n" +
            "      \"original_language\": \"en\",\n" +
            "      \"original_name\": \"Watch What Happens Live with Andy Cohen\",\n" +
            "      \"overview\": \"Bravo network executive Andy Cohen discusses pop culture topics with celebrities and reality show personalities.\",\n" +
            "      \"popularity\": 7285.549,\n" +
            "      \"poster_path\": \"/onSD9UXfJwrMXWhq7UY7hGF2S1h.jpg\",\n" +
            "      \"first_air_date\": \"2009-07-16\",\n" +
            "      \"name\": \"Watch What Happens Live with Andy Cohen\",\n" +
            "      \"vote_average\": 5.078,\n" +
            "      \"vote_count\": 45\n" +
            "    },\n" +
            "    {\n" +
            "      \"adult\": false,\n" +
            "      \"backdrop_path\": \"/dfX2UaHVE5c7kLBFbgmEZJuy4Ev.jpg\",\n" +
            "      \"genre_ids\": [\n" +
            "        10767,\n" +
            "        35\n" +
            "      ],\n" +
            "      \"id\": 61818,\n" +
            "      \"origin_country\": [\n" +
            "        \"US\"\n" +
            "      ],\n" +
            "      \"original_language\": \"en\",\n" +
            "      \"original_name\": \"Late Night with Seth Meyers\",\n" +
            "      \"overview\": \"Seth Meyers, who is \\\"Saturday Night Live’s\\\" longest serving anchor on the show’s wildly popular \\\"Weekend Update,\\\" takes over as host of NBC’s \\\"Late Night\\\" — home to A-list celebrity guests, memorable comedy and the best in musical talent. As the Emmy Award-winning head writer for \\\"SNL,\\\" Meyers has established a reputation for sharp wit and perfectly timed comedy, and has gained fame for his spot-on jokes and satire. Meyers takes his departure from \\\"SNL\\\" to his new post at \\\"Late Night,\\\" as Jimmy Fallon moves to \\\"The Tonight Show\\\".\",\n" +
            "      \"popularity\": 6994.885,\n" +
            "      \"poster_path\": \"/g6MrJxNaHYGYU7Sxo72e5B8gKOV.jpg\",\n" +
            "      \"first_air_date\": \"2014-02-25\",\n" +
            "      \"name\": \"Late Night with Seth Meyers\",\n" +
            "      \"vote_average\": 5.55,\n" +
            "      \"vote_count\": 80\n" +
            "    }\n" +
            "   \n" +
            "  ],\n" +
            "  \"total_pages\": 8479,\n" +
            "  \"total_results\": 169575\n" +
            "}"

}