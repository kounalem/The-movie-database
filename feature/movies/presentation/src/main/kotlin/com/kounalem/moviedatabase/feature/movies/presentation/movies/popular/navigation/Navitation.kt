package com.kounalem.moviedatabase.feature.movies.presentation.movies.popular.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.kounalem.moviedatabase.feature.movies.presentation.movies.popular.PopularMoviesScreen
import com.kounalem.moviedatanase.core.ui.navigation.NavRoute
import com.kounalem.moviedatabase.feature.movies.presentation.movies.details.navigation.Navigation as MovieDetailsNavigation

interface Navigation {
    data object PopularMovies : NavRoute {
        override val path: NavRoute.Path
            get() = NavRoute.Path("movies")
    }
}

fun NavController.navigateToMoviesScreen(navOptions: NavOptions) =
    navigate(Navigation.PopularMovies.path.value, navOptions)

fun navigateToHomeScreen(
    navController: NavHostController, navGraphBuilder: NavGraphBuilder
) {
    navGraphBuilder.composable(
        route = Navigation.PopularMovies.path.value,
        enterTransition = {
            return@composable fadeIn(tween(1000))
        },
        exitTransition = {
            return@composable fadeOut(tween(700))
        },
        popEnterTransition = {
            return@composable slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.End, tween(700)
            )
        }
    ) {

        PopularMoviesScreen(
            favouriteId = navController.currentBackStackEntry
                ?.savedStateHandle
                ?.get<Int>(MovieDetailsNavigation.Details.RESULT_KEY_FAVOURITE_ID),
            favouriteStatus = navController.currentBackStackEntry
                ?.savedStateHandle
                ?.get<Boolean>(MovieDetailsNavigation.Details.RESULT_KEY_FAVOURITE),
            navigateToDetails = { id ->
                navController.navigate(
                    MovieDetailsNavigation.Details.withArgs(
                        id.toString()
                    )
                )
            }
        )
    }
}