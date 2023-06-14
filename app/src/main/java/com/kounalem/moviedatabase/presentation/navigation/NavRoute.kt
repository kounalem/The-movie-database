package com.kounalem.moviedatabase.presentation.navigation

sealed class NavRoute(val path: String) {

    object Home : NavRoute("home")

    object Details : NavRoute("details") {
        const val DETAILS_ID = "id"
    }

    // build navigation path (for screen navigation)
    fun withArgs(vararg args: String): String {
        return buildString {
            append(path)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }

    // build and setup route format (in navigation graph)
    fun withArgsFormat(vararg args: String): String {
        return buildString {
            append(path)
            args.forEach { arg ->
                append("/{$arg}")
            }
        }
    }
}


