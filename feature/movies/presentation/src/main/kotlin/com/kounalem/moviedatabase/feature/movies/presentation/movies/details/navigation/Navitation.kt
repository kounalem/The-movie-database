package com.kounalem.moviedatabase.feature.movies.presentation.movies.details.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.kounalem.moviedatabase.feature.movies.presentation.movies.details.MovieDetails
import com.kounalem.moviedatanase.core.ui.navigation.NavRoute

interface Navigation {
    data object Details : NavRoute {
        const val DETAILS_ID = "id"
        const val RESULT_KEY_FAVOURITE = "favoriteStatusKey"
        const val RESULT_KEY_FAVOURITE_ID = "favoriteStatusID"

        override val path: NavRoute.Path
            get() = NavRoute.Path("details")
    }
}

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
        ),
        enterTransition = {
            return@composable slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Start, tween(700)
            )
        },
        popExitTransition = {
            return@composable slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.End, tween(700)
            )
        },
    ) { navBackStackEntry ->
        val args = navBackStackEntry.arguments
        MovieDetails(
            popBackStack = { isFavourite ->
                // Update the SavedStateHandle with the data you want to pass
                navController.previousBackStackEntry?.savedStateHandle?.set(
                    Navigation.Details.RESULT_KEY_FAVOURITE,
                    isFavourite
                )
                navController.previousBackStackEntry?.savedStateHandle?.set(
                    Navigation.Details.RESULT_KEY_FAVOURITE_ID,
                    args?.getInt(Navigation.Details.DETAILS_ID)
                )
                navController.popBackStack()
            },
        )
    }
}
