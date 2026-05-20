package com.serify.components.commons

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
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
            .height(68.dp),
        containerColor = Color.White,
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
                    imageVector = Icons.Outlined.BookmarkBorder,
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
    selectedIconColor = Color(0xFF111111),
    selectedTextColor = Color(0xFF111111),
    unselectedIconColor = Color(0xFFBDBDBD),
    unselectedTextColor = Color(0xFFBDBDBD),
    indicatorColor = Color.Transparent
)