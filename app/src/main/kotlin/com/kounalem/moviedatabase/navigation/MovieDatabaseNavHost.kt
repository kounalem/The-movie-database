/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kounalem.moviedatabase.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.kounalem.moviedatabase.MovieAppState
import com.kounalem.moviedatabase.feature.movies.presentation.movies.details.navigation.navigateToMovieDetailsScreen
import com.kounalem.moviedatabase.feature.movies.presentation.movies.popular.navigation.Navigation
import com.kounalem.moviedatabase.feature.movies.presentation.movies.popular.navigation.navigateToMoviesScreen
import com.kounalem.moviedatabase.saved.presentation.navigation.navigateToSavedElementsScreen
import com.kounalem.moviedatabase.show.presentation.details.navigation.navigateToShowDetailsScreen
import com.kounalem.moviedatabase.show.presentation.popular.navigation.navigateToShowsScreen

/**
 * Top-level navigation graph. Navigation is organized as explained at
 * https://d.android.com/jetpack/compose/nav-adaptive
 *
 * The navigation graph defined in this file defines the different top level routes. Navigation
 * within each route is handled using state and Back Handlers.
 */
@Composable
fun MovieDatabaseNavHost(
    modifier: Modifier,
    appState: MovieAppState,
    onShowSnackbar: suspend (String, String?) -> Boolean,
) {
    val navController = appState.navController
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Navigation.PopularMovies.path.value
    ) {
        navigateToMoviesScreen(navController, this)
        navigateToMovieDetailsScreen(navController, this)
        navigateToShowsScreen(navController, this)
        navigateToShowDetailsScreen(navController, this)
        navigateToSavedElementsScreen(this)
    }

}
