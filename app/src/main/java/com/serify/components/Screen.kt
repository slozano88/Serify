package com.serify.components

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Explore : Screen("explore")
    object Saved : Screen("saved")
    object Profile : Screen("profile")

    object SeriesDetail : Screen("series_detail/{serieId}") {
        fun createRoute(serieId: Int): String {
            return "series_detail/$serieId"
        }
    }

    object Genre : Screen("genre/{genreName}") {
        fun createRoute(genreName: String): String {
            return "genre/$genreName"
        }
    }
}