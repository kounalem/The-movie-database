package com.kounalem.moviedatabase.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.kounalem.moviedatabase.feature.movies.presentation.movies.details.Navigation as PopularMoviesDetailsNavigation
import com.kounalem.moviedatabase.feature.movies.presentation.movies.details.MovieDetails
import com.kounalem.moviedatabase.feature.movies.presentation.movies.details.Navigation.Details.DETAILS_ID
import com.kounalem.moviedatabase.feature.movies.presentation.movies.details.navigation.navigateToDetailsScreen
import com.kounalem.moviedatabase.feature.movies.presentation.movies.details.navigation.navigateToHomeScreen
import com.kounalem.moviedatabase.feature.movies.presentation.movies.popular.Navigation as PopularMoviesListNavigation
import com.kounalem.moviedatabase.feature.movies.presentation.movies.popular.PopularMoviesScreen

@Composable
internal fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = PopularMoviesListNavigation.Home.path.value
    ) {
        navigateToHomeScreen(navController, this)
        navigateToDetailsScreen(navController, this)
    }
}

private fun popUpToHome(navController: NavHostController) {
    navController.popBackStack(
        PopularMoviesListNavigation.Home.path.value,
        inclusive = false
    )
}
