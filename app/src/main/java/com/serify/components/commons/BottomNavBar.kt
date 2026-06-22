package com.serify.components.commons

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.serify.ui.theme.AppColors

@Composable
fun BottomNavBar(
    selectedItem: String = "Inicio",
    onHomeClick: () -> Unit = {},
    onExploreClick: () -> Unit = {},
    onSavedClick: () -> Unit = {},
    onProfileClick: () -> Unit = {}
) {
    NavigationBar(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
            .border(BorderStroke(1.dp, AppColors.Border)),
        containerColor = AppColors.Surface,
        tonalElevation = 0.dp
    ) {
        NavigationBarItem(
            selected = selectedItem == "Inicio",
            onClick = onHomeClick,
            icon = {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Inicio"
                )
            },
            label = { Text("Inicio") },
            colors = navItemColors()
        )

        NavigationBarItem(
            selected = selectedItem == "Explorar",
            onClick = onExploreClick,
            icon = {
                Icon(
                    imageVector = Icons.Outlined.Search,
                    contentDescription = "Explorar"
                )
            },
            label = { Text("Explorar") },
            colors = navItemColors()
        )

        NavigationBarItem(
            selected = selectedItem == "Guardadas",
            onClick = onSavedClick,
            icon = {
                Icon(
                    imageVector = if (selectedItem == "Guardadas") Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                    contentDescription = "Guardadas"
                )
            },
            label = { Text("Guardadas") },
            colors = navItemColors()
        )

        NavigationBarItem(
            selected = selectedItem == "Perfil",
            onClick = onProfileClick,
            icon = {
                Icon(
                    imageVector = Icons.Outlined.Person,
                    contentDescription = "Perfil"
                )
            },
            label = { Text("Perfil") },
            colors = navItemColors()
        )
    }
}

@Composable
private fun navItemColors() = NavigationBarItemDefaults.colors(
    selectedIconColor = AppColors.AccentBlue,
    selectedTextColor = AppColors.AccentBlue,
    unselectedIconColor = AppColors.TextMuted,
    unselectedTextColor = AppColors.TextMuted,
    indicatorColor = Color.Transparent
)
