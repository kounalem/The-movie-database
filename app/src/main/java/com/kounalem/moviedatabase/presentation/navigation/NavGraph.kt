package com.kounalem.moviedatabase.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.kounalem.moviedatabase.presentation.details.MovieDetails
import com.kounalem.moviedatabase.presentation.navigation.NavRoute.Details.DETAILS_ID
import com.kounalem.moviedatabase.presentation.popular.PopularMoviesScreen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = NavRoute.Home.path
    ) {
        addHomeScreen(navController, this)
        addDetailsScreen(navController, this)
    }
}

private fun addHomeScreen(
    navController: NavHostController,
    navGraphBuilder: NavGraphBuilder
) {
    navGraphBuilder.composable(route = NavRoute.Home.path) {
        PopularMoviesScreen(
            navigateToDetails = { id ->
                navController.navigate(NavRoute.Details.withArgs(id.toString()))
            }
        )
    }
}

private fun addDetailsScreen(
    navController: NavHostController,
    navGraphBuilder: NavGraphBuilder
) {
    navGraphBuilder.composable(
        route = NavRoute.Details.withArgsFormat(DETAILS_ID),
        arguments = listOf(
            navArgument(DETAILS_ID) {
                type = NavType.IntType
                nullable = false
            }
        )
    ) { navBackStackEntry ->
        val args = navBackStackEntry.arguments
        MovieDetails(
            id = args?.getInt(DETAILS_ID)!!,
            popBackStack = { navController.popBackStack() },
        )
    }
}

private fun popUpToHome(navController: NavHostController) {
    navController.popBackStack(NavRoute.Home.path, inclusive = false)
}
