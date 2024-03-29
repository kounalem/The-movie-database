package com.kounalem.moviedatabase.navigation

internal sealed class NavRoute(val path: String) {

    data object Home : NavRoute("home")

    data object Details : NavRoute("details") {
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


