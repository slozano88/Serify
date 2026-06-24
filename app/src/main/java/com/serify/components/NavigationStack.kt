package com.serify.components

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.firebase.auth.FirebaseAuth
import com.serify.components.ai.AiChatScreen
import com.serify.components.explore.ExploreScreen
import com.serify.components.genre.GenreScreen
import com.serify.components.home.HomeScreen
import com.serify.components.login.LoginScreen
import com.serify.components.profile.ProfileScreen
import com.serify.components.saved.SavedScreen
import com.serify.components.seriesdetail.SeriesDetailScreen
import com.serify.components.splash.SplashScreen

@Composable
fun NavigationStack() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(
                onSplashFinished = {
                    val destination = if (FirebaseAuth.getInstance().currentUser != null) {
                        Screen.Home.route
                    } else {
                        Screen.Login.route
                    }

                    navController.navigate(destination) {
                        popUpTo(Screen.Splash.route) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
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
                    navController.navigate(Screen.Explore.route) {
                        launchSingleTop = true
                    }
                },
                onSavedClick = {
                    navController.navigate(Screen.Saved.route) {
                        launchSingleTop = true
                    }
                },
                onProfileClick = {
                    navController.navigate(Screen.Profile.route) {
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(Screen.Explore.route) {
            ExploreScreen(
                onSerieClick = { serieId ->
                    navController.navigate(Screen.SeriesDetail.createRoute(serieId))
                },
                onHomeClick = {
                    navController.navigate(Screen.Home.route) {
                        launchSingleTop = true
                    }
                },
                onSavedClick = {
                    navController.navigate(Screen.Saved.route) {
                        launchSingleTop = true
                    }
                },
                onProfileClick = {
                    navController.navigate(Screen.Profile.route) {
                        launchSingleTop = true
                    }
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
                },
                onHomeClick = {
                    navController.navigate(Screen.Home.route) {
                        launchSingleTop = true
                    }
                },
                onExploreClick = {
                    navController.navigate(Screen.Explore.route) {
                        launchSingleTop = true
                    }
                },
                onSavedClick = {
                    navController.navigate(Screen.Saved.route) {
                        launchSingleTop = true
                    }
                },
                onProfileClick = {
                    navController.navigate(Screen.Profile.route) {
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(
            route = Screen.Genre.route,
            arguments = listOf(
                navArgument("genreName") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val genreName = backStackEntry.arguments?.getString("genreName") ?: "Drama"

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

        composable(Screen.Saved.route) {
            SavedScreen(
                onSerieClick = { serieId ->
                    navController.navigate(Screen.SeriesDetail.createRoute(serieId))
                },
                onHomeClick = {
                    navController.navigate(Screen.Home.route) {
                        launchSingleTop = true
                    }
                },
                onExploreClick = {
                    navController.navigate(Screen.Explore.route) {
                        launchSingleTop = true
                    }
                },
                onSavedClick = {},
                onProfileClick = {
                    navController.navigate(Screen.Profile.route) {
                        launchSingleTop = true
                    }
                },
                onAiChatClick = {
                    navController.navigate(Screen.AiChat.route) {
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(Screen.AiChat.route) {
            AiChatScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.Profile.route) {
            ProfileScreen(
                onHomeClick = {
                    navController.navigate(Screen.Home.route) {
                        launchSingleTop = true
                    }
                },
                onExploreClick = {
                    navController.navigate(Screen.Explore.route) {
                        launchSingleTop = true
                    }
                },
                onSavedClick = {
                    navController.navigate(Screen.Saved.route) {
                        launchSingleTop = true
                    }
                },
                onProfileClick = {
                    navController.navigate(Screen.Profile.route) {
                        launchSingleTop = true
                    }
                },
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}
