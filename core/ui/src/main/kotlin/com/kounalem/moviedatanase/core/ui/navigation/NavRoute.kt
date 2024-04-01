package com.kounalem.moviedatanase.core.ui.navigation

interface NavRoute {
    val path: Path

    @JvmInline
    value class Path(val value: String)


    // build navigation path (for screen navigation)
    fun withArgs(vararg args: String): String {
        return buildString {
            append(path.value)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }

    // build and setup route format (in navigation graph)
    fun withArgsFormat(vararg args: String): String {
        return buildString {
            append(path.value)
            args.forEach { arg ->
                append("/{$arg}")
            }
        }
    }
}