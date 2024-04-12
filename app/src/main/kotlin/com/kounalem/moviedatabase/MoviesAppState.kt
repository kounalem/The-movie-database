package com.kounalem.moviedatabase

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import androidx.tracing.Trace
import com.kounalem.moviedatabase.core.data.NetworkMonitor
import com.kounalem.moviedatabase.feature.movies.presentation.movies.popular.navigation.navigateToMoviesScreen
import com.kounalem.moviedatabase.navigation.TopLevelDestination
import com.kounalem.moviedatabase.show.presentation.popular.navigation.navigateToShowsScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@Composable
fun rememberMovieAppState(
    windowSizeClass: WindowSizeClass,
    networkMonitor: NetworkMonitor,
    navController: NavHostController = rememberNavController(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
): MovieAppState {
    return remember(
        navController,
        windowSizeClass,
    ) {
        MovieAppState(
            navController = navController,
            windowSizeClass = windowSizeClass,
            networkMonitor = networkMonitor,
            coroutineScope = coroutineScope,
        )
    }
}

@Stable
class MovieAppState(
    val navController: NavHostController,
    val windowSizeClass: WindowSizeClass,
    coroutineScope: CoroutineScope,
    networkMonitor: NetworkMonitor,
) {

    val currentDestination: NavDestination?
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination
    /**
     * Map of top level destinations to be used in the TopBar, BottomBar and NavRail. The key is the
     * route.
     */
    val topLevelDestinations: List<TopLevelDestination> = TopLevelDestination.entries

    val shouldShowBottomBar: Boolean
        get() = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact

    val isOffline = networkMonitor.isOnline
        .map(Boolean::not)
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false,
        )

    /**
     * UI logic for navigating to a top level destination in the app. Top level destinations have
     * only one copy of the destination of the back stack, and save and restore state whenever you
     * navigate to and from it.
     *
     * @param topLevelDestination: The destination the app needs to navigate to.
     */
    fun navigateToTopLevelDestination(topLevelDestination: TopLevelDestination) {
        trace("Navigation: ${topLevelDestination.name}") {
            val topLevelNavOptions = navOptions {
                // Pop up to the start destination of the graph to
                // avoid building up a large stack of destinations
                // on the back stack as users select items
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                }
                // Avoid multiple copies of the same destination when
                // reselecting the same item
                launchSingleTop = true
                // Restore state when reselecting a previously selected item
                restoreState = true
            }

//            navigateToHomeScreen(navController, this)
//            navigateToDetailsScreen(navController, this)
            when (topLevelDestination) {
                TopLevelDestination.MOVIES -> navController.navigateToMoviesScreen(topLevelNavOptions)
                TopLevelDestination.SERIES -> navController.navigateToShowsScreen(topLevelNavOptions)
                TopLevelDestination.SAVED -> TODO("")
            }
        }
    }

    private inline fun <T> trace(label: String, block: () -> T): T {
        Trace.beginSection(label)
        try {
            return block()
        } finally {
            Trace.endSection()
        }
    }

}

