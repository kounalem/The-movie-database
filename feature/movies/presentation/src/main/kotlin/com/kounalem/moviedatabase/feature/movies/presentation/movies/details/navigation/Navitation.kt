package com.kounalem.moviedatabase.feature.movies.presentation.movies.details.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.kounalem.moviedatabase.feature.movies.presentation.movies.details.MovieDetails
import com.kounalem.moviedatabase.feature.movies.presentation.movies.details.Navigation

fun navigateToDetailsScreen(
    navController: NavHostController,
    navGraphBuilder: NavGraphBuilder
) {
    navGraphBuilder.composable(
        route = Navigation.Details.withArgsFormat(
            Navigation.Details.DETAILS_ID
        ),
        arguments = listOf(
            navArgument(Navigation.Details.DETAILS_ID) {
                type = NavType.IntType
                nullable = false
            }
        )
    ) { navBackStackEntry ->
        val args = navBackStackEntry.arguments
        MovieDetails(
            id = args?.getInt(Navigation.Details.DETAILS_ID)!!,
            popBackStack = { navController.popBackStack() },
        )
    }
}