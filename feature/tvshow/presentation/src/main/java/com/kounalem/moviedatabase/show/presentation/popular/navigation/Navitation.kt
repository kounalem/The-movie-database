package com.kounalem.moviedatabase.show.presentation.popular.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.kounalem.moviedatabase.show.presentation.popular.PopularShowScreen
import com.kounalem.moviedatanase.core.ui.navigation.NavRoute
import com.kounalem.moviedatabase.show.presentation.details.navigation.Navigation.Details as DetailsNavigation

interface Navigation {
    data object PopularShows : NavRoute {
        override val path: NavRoute.Path
            get() = NavRoute.Path("shows")
    }

}

fun NavController.navigateToShowsScreen(navOptions: NavOptions) =
    navigate(Navigation.PopularShows.path.value, navOptions)


fun navigateToShowsScreen(
    navController: NavHostController,
    navGraphBuilder: NavGraphBuilder
) {
    navGraphBuilder.composable(
        route = Navigation.PopularShows.path.value,
        enterTransition = {
            if (initialState.destination.route != DetailsNavigation.path.value) {
                EnterTransition.None
            } else {
                return@composable fadeIn(tween(1000))
            }
        },
        exitTransition = {
            return@composable fadeOut(tween(700))
        },
        popEnterTransition = {
            if (initialState.destination.route != DetailsNavigation.path.value) {
                EnterTransition.None
            } else {
                return@composable slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.End, tween(700)
                )
            }
        }
        ) {
        PopularShowScreen(
            favouriteId = navController.currentBackStackEntry
                ?.savedStateHandle
                ?.get<Int>(DetailsNavigation.RESULT_KEY_FAVOURITE_ID),
            favouriteStatus = navController.currentBackStackEntry
                ?.savedStateHandle
                ?.get<Boolean>(DetailsNavigation.RESULT_KEY_FAVOURITE),
            navigateToTvShow = { id ->
                navController.navigate(
                    DetailsNavigation.withArgs(
                        id.toString()
                    )
                )
            }
        )
    }
}
