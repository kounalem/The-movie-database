package com.kounalem.moviedatabase.network

data class ServerInfo(
    val id: String,
    val host: String,
    val wsAddress: String,
    val scheme: String,
) {
    companion object {
        private const val ApiVersion = "3"

        fun getInfo(info: String) = when (info) {
            Dev.id -> {
                "${Dev.scheme}://${Dev.host}/$ApiVersion/"
            }

            Staging.id -> {
                "${Staging.scheme}://${Staging.host}/$ApiVersion/"
            }

            Local.id -> {
                "${Local.scheme}://${Local.host}/$ApiVersion/"
            }

            else -> {
                "${Prod.scheme}://${Prod.host}/$ApiVersion/"
            }
        }


        val Prod = ServerInfo(
            id = "prod",
            host = "api.themoviedb.org",
            wsAddress = "http://api.themoviedb.org/3/",
            scheme = "http"
        )

        val Dev = ServerInfo(
            id = "dev",
            host = "api.themoviedb.org",
            wsAddress = "http://api.themoviedb.org/3/",
            scheme = "http"
        )

        val Staging = ServerInfo(
            id = "staging",
            host = "api.themoviedb.org",
            wsAddress = "http://api.themoviedb.org/3/",
            scheme = "http"
        )

        val Local = ServerInfo(
            id = "local",
            host = "api.themoviedb.org",
            wsAddress = "http://api.themoviedb.org/3/",
            scheme = "http"
        )
    }
}
