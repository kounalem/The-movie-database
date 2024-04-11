package com.kounalem.moviedatabase.show.presentation.popular.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.kounalem.moviedatabase.show.presentation.popular.PopularShowScreen
import com.kounalem.moviedatanase.core.ui.navigation.NavRoute


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
    navGraphBuilder.composable(route = Navigation.PopularShows.path.value) {
        PopularShowScreen(
            navigateToTvShow = {
//               TODO  navController.navigate(
//                    Navigation.Details.withArgs(
//                        id.toString()
//                    )
//                )
            }
        )
    }
}
