package com.kounalem.moviedatabase.feature.movies.presentation.movies.details.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.kounalem.moviedatabase.feature.movies.presentation.movies.details.MovieDetails
import com.kounalem.moviedatabase.feature.movies.presentation.movies.details.Navigation
import com.kounalem.moviedatabase.feature.movies.presentation.movies.popular.PopularMoviesScreen

fun navigateToHomeScreen(
    navController: NavHostController,
    navGraphBuilder: NavGraphBuilder
) {
    navGraphBuilder.composable(route = com.kounalem.moviedatabase.feature.movies.presentation.movies.popular.Navigation.Home.path.value) {
        PopularMoviesScreen(
            navigateToDetails = { id ->
                navController.navigate(
                    Navigation.Details.withArgs(
                        id.toString()
                    )
                )
            }
        )
    }
}