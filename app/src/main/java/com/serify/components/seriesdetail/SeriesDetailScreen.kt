package com.serify.components.seriesdetail

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil.compose.AsyncImage
import com.serify.components.commons.BottomNavBar
import com.serify.data.model.CastMember
import com.serify.ui.theme.AppColors

@Composable
fun SeriesDetailScreen(
    serieId: Int,
    viewModel: SeriesDetailScreenViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {},
    onHomeClick: () -> Unit = {},
    onExploreClick: () -> Unit = {},
    onSavedClick: () -> Unit = {},
    onProfileClick: () -> Unit = {}
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(serieId) {
        viewModel.loadSerieById(serieId)
    }

    Scaffold(
        bottomBar = {
            BottomNavBar(
                selectedItem = "",
                onHomeClick = onHomeClick,
                onExploreClick = onExploreClick,
                onSavedClick = onSavedClick,
                onProfileClick = onProfileClick
            )
        },
        containerColor = AppColors.Background
    ) { paddingValues ->

        when {
            state.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(AppColors.Background)
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = AppColors.AccentBlue)
                }
            }

            state.error != null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(AppColors.Background)
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = state.error ?: "Error desconocido",
                        color = AppColors.TextSecondary
                    )
                }
            }

            state.serie != null -> {
                val serie = state.serie

                if (serie != null) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(AppColors.Background)
                            .padding(paddingValues)
                            .verticalScroll(rememberScrollState())
                            .navigationBarsPadding()
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(255.dp)
                                .background(AppColors.SurfaceSoft)
                        ) {
                            AsyncImage(
                                model = serie.imageUrl,
                                contentDescription = serie.name,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )

                            Box(
                                modifier = Modifier
                                    .align(Alignment.BottomCenter)
                                    .fillMaxWidth()
                                    .height(120.dp)
                                    .background(
                                        Brush.verticalGradient(
                                            colors = listOf(
                                                Color.Transparent,
                                                Color(0xF00B1020)
                                            )
                                        )
                                    )
                            )

                            IconButton(
                                onClick = onBackClick,
                                modifier = Modifier
                                    .statusBarsPadding()
                                    .padding(start = 12.dp, top = 8.dp)
                                    .align(Alignment.TopStart)
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xCC0B1020))
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    contentDescription = "Volver",
                                    tint = AppColors.TextPrimary
                                )
                            }

                            IconButton(
                                onClick = { viewModel.toggleSaved() },
                                modifier = Modifier
                                    .statusBarsPadding()
                                    .padding(end = 12.dp, top = 8.dp)
                                    .align(Alignment.TopEnd)
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xCC0B1020))
                            ) {
                                Icon(
                                    imageVector = if (state.isSaved) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                                    contentDescription = if (state.isSaved) "Eliminar de guardadas" else "Guardar",
                                    tint = if (state.isSaved) AppColors.AccentBlue else AppColors.TextPrimary
                                )
                            }

                            Column(
                                modifier = Modifier
                                    .align(Alignment.BottomStart)
                                    .padding(start = 20.dp, bottom = 18.dp, end = 20.dp)
                            ) {
                                Text(
                                    text = serie.name,
                                    color = AppColors.TextPrimary,
                                    fontSize = 23.sp,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 2
                                )

                                Spacer(modifier = Modifier.height(6.dp))

                                Text(
                                    text = buildString {
                                        if (serie.genres.isNotEmpty()) {
                                            append(serie.genres.take(2).joinToString(" · "))
                                        }

                                        if (serie.rating != null) {
                                            if (isNotEmpty()) append(" · ")
                                            append("★ ${serie.rating}")
                                        }
                                    },
                                    color = AppColors.TextPrimary.copy(alpha = 0.9f),
                                    fontSize = 13.sp
                                )
                            }
                        }

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 18.dp)
                        ) {
                            Spacer(modifier = Modifier.height(14.dp))

                            Button(
                                onClick = { viewModel.toggleSaved() },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(42.dp),
                                shape = RoundedCornerShape(22.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (state.isSaved) AppColors.SurfaceSoft else AppColors.PrimaryBlue,
                                    contentColor = AppColors.TextPrimary
                                ),
                                border = BorderStroke(
                                    width = 1.dp,
                                    color = if (state.isSaved) AppColors.Border else AppColors.PrimaryBlue
                                ),
                                elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
                            ) {
                                Icon(
                                    imageVector = if (state.isSaved) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                                    contentDescription = "Guardar",
                                    tint = if (state.isSaved) AppColors.AccentBlue else AppColors.TextPrimary,
                                    modifier = Modifier
                                        .width(17.dp)
                                        .height(17.dp)
                                )

                                Spacer(modifier = Modifier.width(6.dp))

                                Text(
                                    text = if (state.isSaved) "Guardada en mi lista" else "+ Guardar en mi lista",
                                    fontSize = 13.sp
                                )
                            }

                            if (state.saveMessage != null) {
                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = state.saveMessage ?: "",
                                    color = AppColors.TextSecondary,
                                    fontSize = 12.sp
                                )
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                DetailChip(
                                    text = serie.status ?: "Sin estado",
                                    modifier = Modifier.weight(1f)
                                )

                                DetailChip(
                                    text = serie.premiered ?: "Sin fecha",
                                    modifier = Modifier.weight(1f)
                                )

                                DetailChip(
                                    text = if (serie.rating != null) "★ ${serie.rating}" else "Sin calificación",
                                    modifier = Modifier.weight(1f)
                                )
                            }

                            Spacer(modifier = Modifier.height(20.dp))

                            SectionTitle(text = "Sinopsis")

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = cleanHtml(serie.summary),
                                color = AppColors.TextSecondary,
                                fontSize = 14.sp,
                                lineHeight = 21.sp
                            )

                            Spacer(modifier = Modifier.height(20.dp))

                            SectionTitle(text = "Reparto principal")

                            Spacer(modifier = Modifier.height(10.dp))

                            if (state.cast.isEmpty()) {
                                Text(
                                    text = "Sin información de reparto disponible.",
                                    color = AppColors.TextSecondary,
                                    fontSize = 13.sp
                                )
                            } else {
                                LazyRow(
                                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                                    contentPadding = PaddingValues(end = 12.dp)
                                ) {
                                    items(state.cast) { castMember ->
                                        CastMemberCard(castMember = castMember)
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            SectionTitle(text = "Temporadas")

                            Spacer(modifier = Modifier.height(10.dp))

                            if (state.seasons.isEmpty()) {
                                Text(
                                    text = "Sin información de temporadas disponible.",
                                    color = AppColors.TextSecondary,
                                    fontSize = 13.sp
                                )
                            } else {
                                state.seasons.forEach { season ->

                                    SeasonCard(
                                        season = season,
                                        isSelected = state.selectedSeason == season.number,
                                        onClick = {
                                            season.number?.let { seasonNumber ->
                                                viewModel.selectSeason(seasonNumber)
                                            }
                                        }
                                    )

                                    if (state.selectedSeason == season.number) {

                                        Spacer(modifier = Modifier.height(10.dp))

                                        Text(
                                            text = "Episodios - Temporada ${season.number}",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp,
                                            color = AppColors.TextPrimary,
                                            modifier = Modifier.padding(start = 4.dp)
                                        )

                                        Spacer(modifier = Modifier.height(8.dp))

                                        if (state.isTranslatingEpisodes && !state.translatedSeasonNumbers.contains(season.number)) {
                                            Text(
                                                text = "Traduciendo episodios...",
                                                color = AppColors.TextSecondary,
                                                fontSize = 12.sp,
                                                modifier = Modifier.padding(start = 4.dp, bottom = 6.dp)
                                            )
                                        }

                                        val filteredEpisodes = state.episodes.filter { episode ->
                                            episode.season == season.number
                                        }

                                        if (filteredEpisodes.isEmpty()) {
                                            Text(
                                                text = "Sin episodios disponibles para esta temporada.",
                                                color = AppColors.TextSecondary,
                                                fontSize = 13.sp,
                                                modifier = Modifier.padding(start = 4.dp)
                                            )
                                        } else {
                                            filteredEpisodes.forEach { episode ->
                                                EpisodeCard(
                                                    episode = episode
                                                )
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(12.dp))
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(40.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        fontWeight = FontWeight.Bold,
        fontSize = 15.sp,
        color = AppColors.TextPrimary
    )
}

@Composable
private fun CastMemberCard(
    castMember: CastMember
) {
    Column(
        modifier = Modifier.width(92.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = castMember.imageUrl,
            contentDescription = castMember.personName,
            modifier = Modifier
                .width(72.dp)
                .height(72.dp)
                .clip(CircleShape)
                .background(AppColors.SurfaceSoft),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = castMember.personName,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = AppColors.TextPrimary,
            maxLines = 1
        )

        Text(
            text = castMember.characterName ?: "",
            fontSize = 11.sp,
            color = AppColors.TextSecondary,
            maxLines = 1
        )
    }
}

@Composable
private fun DetailChip(
    text: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(43.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = AppColors.Surface
        ),
        border = BorderStroke(
            width = 1.dp,
            color = AppColors.Border
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                color = AppColors.TextSecondary,
                fontSize = 11.sp,
                maxLines = 1
            )
        }
    }
}

private fun cleanHtml(text: String?): String {
    return text
        ?.replace("<br />", "\n")
        ?.replace("<br/>", "\n")
        ?.replace("<br>", "\n")
        ?.replace(Regex("<[^>]*>"), "")
        ?.replace("&amp;", "&")
        ?.replace("&quot;", "\"")
        ?.replace("&#39;", "'")
        ?.replace("&apos;", "'")
        ?.replace("&nbsp;", " ")
        ?.replace("&rsquo;", "’")
        ?.replace("&lsquo;", "‘")
        ?.replace("&rdquo;", "”")
        ?.replace("&ldquo;", "“")
        ?.replace("&ndash;", "–")
        ?.replace("&mdash;", "—")
        ?.trim()
        ?.takeIf { it.isNotBlank() }
        ?: "Sin descripción disponible."
}
