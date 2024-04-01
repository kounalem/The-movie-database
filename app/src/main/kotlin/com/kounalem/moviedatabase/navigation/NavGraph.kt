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
import com.kounalem.moviedatabase.feature.movies.presentation.movies.popular.Navigation as PopularMoviesListNavigation
import com.kounalem.moviedatabase.feature.movies.presentation.movies.popular.PopularMoviesScreen

@Composable
internal fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = PopularMoviesListNavigation.Home.path.value
    ) {
        addHomeScreen(navController, this)
        addDetailsScreen(navController, this)
    }
}

private fun addHomeScreen(
    navController: NavHostController,
    navGraphBuilder: NavGraphBuilder
) {
    navGraphBuilder.composable(route = PopularMoviesListNavigation.Home.path.value) {
        PopularMoviesScreen(
            navigateToDetails = { id ->
                navController.navigate(
                    PopularMoviesDetailsNavigation.Details.withArgs(
                        id.toString()
                    )
                )
            }
        )
    }
}

private fun addDetailsScreen(
    navController: NavHostController,
    navGraphBuilder: NavGraphBuilder
) {
    navGraphBuilder.composable(
        route = PopularMoviesDetailsNavigation.Details.withArgsFormat(
            DETAILS_ID
        ),
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
    navController.popBackStack(
        PopularMoviesListNavigation.Home.path.value,
        inclusive = false
    )
}
