package com.serify.components.explore

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.serify.components.commons.BottomNavBar
import com.serify.components.commons.SeriesGridCard
import com.serify.data.util.SpanishTextMapper
import com.serify.ui.theme.AppColors

@Composable
fun ExploreScreen(
    viewModel: ExploreScreenViewModel = hiltViewModel(),
    onSerieClick: (Int) -> Unit = {},
    onHomeClick: () -> Unit = {},
    onSavedClick: () -> Unit = {},
    onProfileClick: () -> Unit = {}
) {
    val state by viewModel.state.collectAsState()

    val genres = listOf(
        "Todos",
        "Drama",
        "Thriller",
        "Science-Fiction",
        "Comedy",
        "Crime",
        "Action",
        "Adventure",
        "Fantasy",
        "Mystery",
        "Romance"
    )

    Scaffold(
        bottomBar = {
            BottomNavBar(
                selectedItem = "Explorar",
                onHomeClick = onHomeClick,
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
                .padding(horizontal = 18.dp)
        ) {
            Spacer(modifier = Modifier.height(22.dp))

            Text(
                text = "Explorar",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 28.sp,
                color = AppColors.TextPrimary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth(0.36f)
                    .height(7.dp)
                    .background(AppColors.PrimaryBlue, RoundedCornerShape(50.dp))
            )

            Spacer(modifier = Modifier.height(14.dp))

            OutlinedTextField(
                value = state.searchQuery,
                onValueChange = { query ->
                    viewModel.onSearchChange(query)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                placeholder = {
                    Text(
                        text = "Buscar serie...",
                        color = AppColors.TextMuted,
                        fontSize = 13.sp
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Search,
                        contentDescription = "Buscar",
                        tint = AppColors.TextSecondary
                    )
                },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.GridView,
                        contentDescription = "Vista",
                        tint = AppColors.PrimaryBlue
                    )
                },
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = AppColors.TextPrimary,
                    unfocusedTextColor = AppColors.TextPrimary,
                    focusedBorderColor = AppColors.PrimaryBlue,
                    unfocusedBorderColor = AppColors.Border,
                    focusedContainerColor = AppColors.Surface,
                    unfocusedContainerColor = AppColors.Surface,
                    cursorColor = AppColors.AccentBlue
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 0.dp)
            ) {
                items(genres) { genre ->
                    FilterChip(
                        selected = state.selectedGenre == genre,
                        onClick = {
                            viewModel.onGenreSelected(genre)
                        },
                        label = {
                            Text(
                                text = SpanishTextMapper.genreForDisplay(genre),
                                fontSize = 12.sp
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = AppColors.PrimaryBlue,
                            selectedLabelColor = AppColors.TextPrimary,
                            containerColor = AppColors.Surface,
                            labelColor = AppColors.TextSecondary
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = state.selectedGenre == genre,
                            borderColor = AppColors.Border,
                            selectedBorderColor = AppColors.PrimaryBlue
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            when {
                state.isLoading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = AppColors.AccentBlue)
                    }
                }

                state.error != null -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = state.error ?: "Error desconocido",
                            color = AppColors.TextSecondary
                        )
                    }
                }

                else -> {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(14.dp),
                        contentPadding = PaddingValues(bottom = 24.dp)
                    ) {
                        items(state.series) { serie ->
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
            }
        }
    }
}
