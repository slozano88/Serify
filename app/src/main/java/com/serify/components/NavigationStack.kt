package com.example.serify.components

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.serify.components.home.HomeScreen
import com.serify.components.Screen

@Composable
fun NavigationStack() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
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

        composable(Screen.Explore.route) {
            HomeScreen()
        }

        composable(Screen.Saved.route) {
            HomeScreen()
        }

        composable(Screen.Profile.route) {
            HomeScreen()
        }
    }
}