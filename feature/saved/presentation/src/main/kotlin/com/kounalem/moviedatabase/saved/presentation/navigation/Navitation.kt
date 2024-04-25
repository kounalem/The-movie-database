package com.kounalem.moviedatabase.saved.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.kounalem.moviedatabase.saved.presentation.SavedElementsScreen
import com.kounalem.moviedatabase.core.ui.navigation.NavRoute

interface Navigation {
    data object SavedElements : NavRoute {
        override val path: NavRoute.Path
            get() = NavRoute.Path("Saved")
    }
}

fun NavController.navigateToSavedElementsScreen(navOptions: NavOptions) = navigate(Navigation.SavedElements.path.value, navOptions)

fun navigateToSavedElementsScreen(navGraphBuilder: NavGraphBuilder) {
    navGraphBuilder.composable(route = Navigation.SavedElements.path.value) {
        SavedElementsScreen()
    }
}
