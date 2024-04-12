package com.kounalem.moviedatabase.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.kounalem.moviedatabase.feature.movies.presentation.movies.details.navigation.navigateToDetailsScreen
import com.kounalem.moviedatabase.feature.movies.presentation.movies.popular.navigation.navigateToHomeScreen
import com.kounalem.moviedatabase.feature.movies.presentation.movies.popular.navigation.Navigation as PopularMoviesListNavigation

@Composable
internal fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = PopularMoviesListNavigation.PopularMovies.path.value
    ) {
        navigateToHomeScreen(navController, this)
        navigateToDetailsScreen(navController, this)
    }
}

private fun popUpToHome(navController: NavHostController) {
    navController.popBackStack(
        PopularMoviesListNavigation.PopularMovies.path.value,
        inclusive = false
    )
}
