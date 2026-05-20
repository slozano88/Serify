package com.serify.components.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.serify.components.commons.BottomNavBar

@Composable
fun ProfileScreen(
    viewModel: ProfileScreenViewModel = viewModel(),
    onHomeClick: () -> Unit = {},
    onExploreClick: () -> Unit = {},
    onSavedClick: () -> Unit = {},
    onProfileClick: () -> Unit = {}
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        bottomBar = {
            BottomNavBar(
                selectedItem = "Perfil",
                onHomeClick = onHomeClick,
                onExploreClick = onExploreClick,
                onSavedClick = onSavedClick,
                onProfileClick = onProfileClick
            )
        },
        containerColor = Color(0xFFF5F5F5)
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
                .padding(paddingValues)
                .navigationBarsPadding()
        ) {
            ProfileHeader(
                userName = state.userName,
                email = state.email,
                photoUrl = state.photoUrl
            )

            Spacer(modifier = Modifier.height(20.dp))

            SectionTitle(text = "ESTADÍSTICAS")

            Spacer(modifier = Modifier.height(10.dp))

            StatsCard(
                saved = state.savedCount,
                watched = state.watchedCount,
                ratings = state.ratingsCount
            )

            Spacer(modifier = Modifier.height(22.dp))

            SectionTitle(text = "OPCIONES")

            Spacer(modifier = Modifier.height(10.dp))

            OptionsCard()
        }
    }
}

@Composable
private fun ProfileHeader(
    userName: String,
    email: String,
    photoUrl: String?
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(210.dp)
            .background(Color.White)
            .statusBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (photoUrl != null) {
            AsyncImage(
                model = photoUrl,
                contentDescription = userName,
                modifier = Modifier
                    .width(88.dp)
                    .height(88.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFA6A6A6)),
                contentScale = ContentScale.Crop
            )
        } else {
            Box(
                modifier = Modifier
                    .width(88.dp)
                    .height(88.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFA6A6A6))
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = userName,
            color = Color(0xFF222222),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = email,
            color = Color(0xFF777777),
            fontSize = 13.sp
        )
    }
}

@Composable
private fun SectionTitle(
    text: String
) {
    Text(
        text = text,
        modifier = Modifier.padding(horizontal = 18.dp),
        color = Color(0xFF444444),
        fontSize = 13.sp,
        fontWeight = FontWeight.Bold
    )
}

@Composable
private fun StatsCard(
    saved: Int,
    watched: Int,
    ratings: Int
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp)
            .height(108.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        border = BorderStroke(
            width = 1.dp,
            color = Color(0xFFD8D8D8)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            StatItem(
                value = saved,
                label = "Guardadas",
                type = "saved"
            )

            StatItem(
                value = watched,
                label = "Vistas",
                type = "watched"
            )

            StatItem(
                value = ratings,
                label = "Valoraciones",
                type = "ratings"
            )
        }
    }
}

@Composable
private fun StatItem(
    value: Int,
    label: String,
    type: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val icon = when (type) {
            "saved" -> Icons.Outlined.BookmarkBorder
            "watched" -> Icons.Outlined.Visibility
            else -> Icons.Outlined.StarBorder
        }

        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = Color(0xFFA6A6A6),
            modifier = Modifier
                .width(26.dp)
                .height(26.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = value.toString(),
            color = Color(0xFF222222),
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = label,
            color = Color(0xFF555555),
            fontSize = 11.sp
        )
    }
}

@Composable
private fun OptionsCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        border = BorderStroke(
            width = 1.dp,
            color = Color(0xFFD8D8D8)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column {

            ProfileOptionItem(
                title = "Preferencias",
                iconType = "preferences"
            )

            Divider(color = Color(0xFFEDEDED))

            ProfileOptionItem(
                title = "Notificaciones",
                iconType = "notifications"
            )

            Divider(color = Color(0xFFEDEDED))

            ProfileOptionItem(
                title = "Historial",
                iconType = "history"
            )

        }
    }
}

@Composable
private fun ProfileOptionItem(
    title: String,
    iconType: String
) {
    val icon = when (iconType) {
        "profile" -> Icons.Outlined.Person
        "preferences" -> Icons.Outlined.Tune
        "notifications" -> Icons.Outlined.Notifications
        "history" -> Icons.Outlined.History
        else -> Icons.Outlined.Settings
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .padding(horizontal = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = Color(0xFFA6A6A6),
            modifier = Modifier
                .width(22.dp)
                .height(22.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = title,
            color = Color(0xFF222222),
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.weight(1f)
        )

        Icon(
            imageVector = Icons.Outlined.ChevronRight,
            contentDescription = "Abrir",
            tint = Color(0xFFC7C7C7),
            modifier = Modifier
                .width(22.dp)
                .height(22.dp)
        )
    }
}