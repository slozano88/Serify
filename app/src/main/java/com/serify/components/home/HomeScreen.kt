package com.serify.components.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.serify.components.commons.BottomNavBar
import com.serify.components.commons.FeaturedSeriesCard
import com.serify.components.commons.SeriesGridCard
import com.serify.components.commons.TodayTvCard
import com.serify.components.commons.TrendingSeriesItem
import com.serify.ui.theme.AppColors

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
        containerColor = AppColors.Background
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(AppColors.Background)
                .statusBarsPadding()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 18.dp)
        ) {
            Spacer(modifier = Modifier.height(34.dp))

            Text(
                text = "Serify",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 28.sp,
                color = AppColors.TextPrimary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .width(118.dp)
                    .height(7.dp)
                    .background(AppColors.PrimaryBlue, RoundedCornerShape(50.dp))
            )

            Spacer(modifier = Modifier.height(22.dp))

            if (state.recentlyViewed.isNotEmpty()) {
                Text(
                    text = "Vistas recientemente",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = AppColors.TextPrimary
                )

                Spacer(modifier = Modifier.height(10.dp))

                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(
                        items = state.recentlyViewed,
                        key = { serie -> serie.id }
                    ) { serie ->
                        Box(modifier = Modifier.width(150.dp)) {
                            SeriesGridCard(
                                serie = serie,
                                isSaved = state.savedSerieIds.contains(serie.id),
                                onClick = {
                                    onSerieClick(serie.id)
                                },
                                onSaveClick = {
                                    viewModel.toggleSaved(serie)
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(22.dp))
            }

            Text(
                text = "Nuevo hoy",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = AppColors.TextPrimary
            )

            Spacer(modifier = Modifier.height(10.dp))

            if (state.todayTv.isEmpty()) {
                Text(
                    text = "No hay estrenos o emisiones disponibles para hoy.",
                    color = AppColors.TextSecondary,
                    fontSize = 13.sp
                )
            } else {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(horizontal = 0.dp)
                ) {
                    items(state.todayTv) { item ->
                        TodayTvCard(
                            item = item,
                            isSaved = item.showId?.let { state.savedSerieIds.contains(it) } == true,
                            onClick = {
                                item.showId?.let { id ->
                                    onSerieClick(id)
                                }
                            },
                            onSaveClick = {
                                item.showId?.let { id ->
                                    viewModel.toggleSavedById(id)
                                }
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            when {
                state.isLoading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(192.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = AppColors.AccentBlue)
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
                            color = AppColors.TextSecondary
                        )
                    }
                }

                state.featuredSerie != null -> {
                    val featured = state.featuredSerie!!
                    FeaturedSeriesCard(
                        serie = featured,
                        isSaved = state.savedSerieIds.contains(featured.id),
                        onClick = {
                            onSerieClick(featured.id)
                        },
                        onSaveClick = {
                            viewModel.toggleSaved(featured)
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Tendencias principales",
                    fontWeight = FontWeight.Bold,
                    color = AppColors.TextPrimary,
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                state.trendingSeries.forEachIndexed { index, serie ->
                    TrendingSeriesItem(
                        ranking = "0${index + 1}",
                        serie = serie,
                        isSaved = state.savedSerieIds.contains(serie.id),
                        onClick = {
                            onSerieClick(serie.id)
                        },
                        onSaveClick = {
                            viewModel.toggleSaved(serie)
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
