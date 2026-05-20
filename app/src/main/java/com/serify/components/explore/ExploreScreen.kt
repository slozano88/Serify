package com.serify.components.explore

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.serify.components.commons.BottomNavBar
import com.serify.components.commons.SeriesGridCard

@Composable
fun ExploreScreen(
    viewModel: ExploreScreenViewModel = viewModel(),
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
        containerColor = Color.White
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .statusBarsPadding()
                .padding(paddingValues)
                .padding(horizontal = 18.dp)
        ) {
            Spacer(modifier = Modifier.height(22.dp))

            Text(
                text = "Serify",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 26.sp,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth(0.38f)
                    .height(7.dp)
                    .background(Color(0xFFD9D9D9))
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = state.searchQuery,
                onValueChange = { query ->
                    viewModel.onSearchChange(query)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                placeholder = {
                    Text(
                        text = "Buscar serie...",
                        color = Color(0xFFB0B0B0),
                        fontSize = 13.sp
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Search,
                        contentDescription = "Buscar",
                        tint = Color(0xFFB0B0B0)
                    )
                },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.GridView,
                        contentDescription = "Vista",
                        tint = Color(0xFF9A9A9A)
                    )
                },
                singleLine = true,
                shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFD7D7D7),
                    unfocusedBorderColor = Color(0xFFD7D7D7),
                    focusedContainerColor = Color(0xFFF7F7F7),
                    unfocusedContainerColor = Color(0xFFF7F7F7)
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

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
                                text = if (genre == "Science-Fiction") "Sci-Fi" else genre,
                                fontSize = 12.sp
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Color.Black,
                            selectedLabelColor = Color.White,
                            containerColor = Color.White,
                            labelColor = Color(0xFF9A9A9A)
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = state.selectedGenre == genre,
                            borderColor = Color(0xFFD7D7D7),
                            selectedBorderColor = Color.Black
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            when {
                state.isLoading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Color.Black)
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
                            color = Color.Gray
                        )
                    }
                }

                else -> {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        horizontalArrangement = Arrangement.spacedBy(14.dp),
                        contentPadding = PaddingValues(bottom = 24.dp)
                    ) {
                        items(state.series) { serie ->
                            SeriesGridCard(
                                serie = serie,
                                onClick = {
                                    onSerieClick(serie.id)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}