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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.PlayArrow
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.serify.components.commons.BottomNavBar
import com.serify.data.model.CastMember
import com.serify.components.seriesdetail.SeasonCard
import com.serify.components.seriesdetail.EpisodeCard

@Composable
fun SeriesDetailScreen(
    serieId: Int,
    viewModel: SeriesDetailScreenViewModel = viewModel(),
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
                selectedItem = "Guardadas",
                onHomeClick = onHomeClick,
                onExploreClick = onExploreClick,
                onSavedClick = onSavedClick,
                onProfileClick = onProfileClick
            )
        },
        containerColor = Color.White
    ) { paddingValues ->

        when {
            state.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White)
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color.Black)
                }
            }

            state.error != null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White)
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = state.error ?: "Error desconocido",
                        color = Color.Gray
                    )
                }
            }

            state.serie != null -> {
                val serie = state.serie

                if (serie != null) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.White)
                            .padding(paddingValues)
                            .verticalScroll(rememberScrollState())
                            .navigationBarsPadding()
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(255.dp)
                                .background(Color(0xFFD1D1D1))
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
                                    .height(110.dp)
                                    .background(
                                        Brush.verticalGradient(
                                            colors = listOf(
                                                Color.Transparent,
                                                Color(0xDD000000)
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
                                    .width(34.dp)
                                    .height(34.dp)
                                    .clip(CircleShape)
                                    .background(Color(0x99000000))
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    contentDescription = "Volver",
                                    tint = Color.White
                                )
                            }

                            IconButton(
                                onClick = { },
                                modifier = Modifier
                                    .statusBarsPadding()
                                    .padding(end = 12.dp, top = 8.dp)
                                    .align(Alignment.TopEnd)
                                    .width(34.dp)
                                    .height(34.dp)
                                    .clip(CircleShape)
                                    .background(Color(0x99000000))
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.BookmarkBorder,
                                    contentDescription = "Guardar",
                                    tint = Color.White
                                )
                            }

                            Column(
                                modifier = Modifier
                                    .align(Alignment.BottomStart)
                                    .padding(start = 20.dp, bottom = 18.dp, end = 70.dp)
                            ) {
                                Text(
                                    text = serie.name,
                                    color = Color.White,
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
                                    color = Color.White.copy(alpha = 0.9f),
                                    fontSize = 13.sp
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .padding(end = 18.dp, bottom = 22.dp)
                                    .width(42.dp)
                                    .height(42.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xCC333333)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = "Reproducir",
                                    tint = Color.White
                                )
                            }
                        }

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 18.dp)
                        ) {
                            Spacer(modifier = Modifier.height(12.dp))

                            Button(
                                onClick = { },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(40.dp),
                                shape = RoundedCornerShape(22.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.White,
                                    contentColor = Color(0xFF555555)
                                ),
                                border = BorderStroke(
                                    width = 1.dp,
                                    color = Color(0xFFD0D0D0)
                                ),
                                elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.BookmarkBorder,
                                    contentDescription = "Guardar",
                                    tint = Color(0xFF555555),
                                    modifier = Modifier
                                        .width(16.dp)
                                        .height(16.dp)
                                )

                                Spacer(modifier = Modifier.width(6.dp))

                                Text(
                                    text = "+ Guardar en mi lista",
                                    fontSize = 13.sp
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
                                    text = if (serie.rating != null) "★ ${serie.rating}" else "Sin rating",
                                    modifier = Modifier.weight(1f)
                                )
                            }

                            Spacer(modifier = Modifier.height(18.dp))

                            Text(
                                text = "Sinopsis",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = Color.Black
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = cleanHtml(serie.summary),
                                color = Color(0xFF444444),
                                fontSize = 14.sp,
                                lineHeight = 21.sp
                            )

                            Spacer(modifier = Modifier.height(18.dp))

                            Text(
                                text = "Reparto principal",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = Color.Black
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            if (state.cast.isEmpty()) {
                                Text(
                                    text = "Sin información de reparto disponible.",
                                    color = Color.Gray,
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

                            Text(
                                text = "Temporadas",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = Color.Black
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            if (state.seasons.isEmpty()) {
                                Text(
                                    text = "Sin información de temporadas disponible.",
                                    color = Color.Gray,
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
                                            color = Color(0xFF444444),
                                            modifier = Modifier.padding(start = 4.dp)
                                        )

                                        Spacer(modifier = Modifier.height(8.dp))

                                        val filteredEpisodes = state.episodes.filter { episode ->
                                            episode.season == season.number
                                        }

                                        if (filteredEpisodes.isEmpty()) {
                                            Text(
                                                text = "Sin episodios disponibles para esta temporada.",
                                                color = Color.Gray,
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
                .background(Color(0xFFD1D1D1)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = castMember.personName,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF222222),
            maxLines = 1
        )

        Text(
            text = castMember.characterName ?: "",
            fontSize = 11.sp,
            color = Color(0xFF888888),
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
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        border = BorderStroke(
            width = 1.dp,
            color = Color(0xFFD7D7D7)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                color = Color(0xFF777777),
                fontSize = 11.sp,
                maxLines = 1
            )
        }
    }
}

private fun cleanHtml(text: String?): String {
    return text
        ?.replace("<p>", "")
        ?.replace("</p>", "")
        ?.replace("<b>", "")
        ?.replace("</b>", "")
        ?.replace("<i>", "")
        ?.replace("</i>", "")
        ?.replace("<br>", "\n")
        ?: "Sin descripción disponible."
}