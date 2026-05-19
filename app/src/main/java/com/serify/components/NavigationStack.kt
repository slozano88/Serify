package com.serify.components
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.serify.components.home.HomeScreen
import com.serify.components.seriesdetail.SeriesDetailScreen
import androidx.navigation.navArgument
import com.serify.components.genre.GenreScreen

@Composable
fun NavigationStack() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route

    ) {composable(
        route = Screen.Genre.route,
        arguments = listOf(
            navArgument("genreName") {
                type = NavType.StringType
            }
        )
    ) { backStackEntry ->
        val genreName = backStackEntry.arguments?.getString("genreName") ?: "drama"

        GenreScreen(
            genreName = genreName,
            onSerieClick = { serieId ->
                navController.navigate(Screen.SeriesDetail.createRoute(serieId))
            },
            onBackClick = {
                navController.popBackStack()
            }
        )
    }
        composable(Screen.Home.route) {
            HomeScreen(
                onSerieClick = { serieId ->
                    navController.navigate(Screen.SeriesDetail.createRoute(serieId))
                },
                onGenreClick = { genreName ->
                    navController.navigate(Screen.Genre.createRoute(genreName))
                },
                onExploreClick = {
                    navController.navigate(Screen.Explore.route)
                },
                onSavedClick = {
                    navController.navigate(Screen.Saved.route)
                },
                onProfileClick = {
                    navController.navigate(Screen.Profile.route)
                }
            )
        }

        composable(
            route = Screen.SeriesDetail.route,
            arguments = listOf(
                navArgument("serieId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val serieId = backStackEntry.arguments?.getInt("serieId") ?: 0

            SeriesDetailScreen(
                serieId = serieId,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.Explore.route) {
            HomeScreen(
                onSerieClick = { serieId ->
                    navController.navigate(Screen.SeriesDetail.createRoute(serieId))
                }
            )
        }

        composable(Screen.Saved.route) {
            HomeScreen(
                onSerieClick = { serieId ->
                    navController.navigate(Screen.SeriesDetail.createRoute(serieId))
                }
            )
        }

        composable(Screen.Profile.route) {
            HomeScreen(
                onSerieClick = { serieId ->
                    navController.navigate(Screen.SeriesDetail.createRoute(serieId))
                }
            )
        }
    }
}