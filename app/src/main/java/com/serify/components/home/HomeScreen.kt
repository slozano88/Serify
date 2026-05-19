package com.serify.components.home

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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.serify.components.commons.BottomNavBar
import com.serify.components.commons.CategoryCard
import com.serify.components.commons.FeaturedSeriesCard
import com.serify.components.commons.TrendingSeriesItem
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.unit.sp

@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModel = viewModel(),
    onSerieClick: (Int) -> Unit = {},
    onGenreClick: (String) -> Unit = {},
    onExploreClick: () -> Unit = {},
    onSavedClick: () -> Unit = {},
    onProfileClick: () -> Unit = {}
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        bottomBar = {
            BottomNavBar(
                selectedItem = "Inicio",
                onExploreClick = onExploreClick,
                onSavedClick = onSavedClick,
                onProfileClick = onProfileClick
            )
        },
        containerColor = Color.White
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .statusBarsPadding()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 18.dp)
        ) {
            Spacer(modifier = Modifier.height(34.dp))

            Text(
                text = "Serify",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 26.sp,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .width(116.dp)
                    .height(8.dp)
                    .background(Color(0xFFD9D9D9))
            )

            Spacer(modifier = Modifier.height(22.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                CategoryCard(
                    genre = "Drama",
                    onClick = { onGenreClick("Drama") }
                )

                CategoryCard(
                    genre = "Crime",
                    onClick = { onGenreClick("Crime") }
                )

                CategoryCard(
                    genre = "Comedy",
                    onClick = { onGenreClick("Comedy") }
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            when {
                state.isLoading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(192.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Color.Black)
                    }
                }

                state.error != null -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(192.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = state.error!!,
                            color = Color.Gray
                        )
                    }
                }

                state.featuredSerie != null -> {
                    FeaturedSeriesCard(
                        serie = state.featuredSerie!!,
                        onClick = {
                            onSerieClick(state.featuredSerie!!.id)
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Tendencias",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF222222)
                )

            }

            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                state.trendingSeries.forEachIndexed { index, serie ->
                    TrendingSeriesItem(
                        ranking = "0${index + 1}",
                        serie = serie,
                        onClick = {
                            onSerieClick(serie.id)
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}