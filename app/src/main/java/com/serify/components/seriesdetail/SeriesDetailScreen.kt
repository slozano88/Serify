package com.serify.components.seriesdetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

@Composable
fun SeriesDetailScreen(
    serieId: Int,
    viewModel: SeriesDetailScreenViewModel = viewModel(),
    onBackClick: () -> Unit = {}
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(serieId) {
        viewModel.loadSerieById(serieId)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()
    ) {
        when {
            state.isLoading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = Color.Black
                )
            }

            state.error != null -> {
                Text(
                    text = state.error ?: "Error desconocido",
                    modifier = Modifier.align(Alignment.Center),
                    color = Color.Gray
                )
            }

            state.serie != null -> {
                val serie = state.serie

                if (serie != null) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(horizontal = 18.dp)
                    ) {
                        IconButton(
                            onClick = onBackClick,
                            modifier = Modifier.padding(top = 12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Volver",
                                tint = Color.Black
                            )
                        }

                        AsyncImage(
                            model = serie.imageUrl,
                            contentDescription = serie.name,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(320.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(Color(0xFFD1D1D1)),
                            contentScale = ContentScale.Crop
                        )

                        Spacer(modifier = Modifier.height(18.dp))

                        Text(
                            text = serie.name,
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = buildString {
                                if (serie.genres.isNotEmpty()) {
                                    append(serie.genres.joinToString(" · "))
                                }

                                if (serie.rating != null) {
                                    if (isNotEmpty()) append(" · ")
                                    append("★ ${serie.rating}")
                                }
                            },
                            color = Color(0xFF777777),
                            fontSize = 14.sp
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Estreno: ${serie.premiered ?: "Sin fecha"}",
                            color = Color(0xFF777777),
                            fontSize = 14.sp
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "Estado: ${serie.status ?: "Sin información"}",
                            color = Color(0xFF777777),
                            fontSize = 14.sp
                        )

                        Spacer(modifier = Modifier.height(18.dp))

                        Text(
                            text = "Sinopsis",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = cleanHtml(serie.summary),
                            color = Color(0xFF333333),
                            fontSize = 15.sp,
                            lineHeight = 22.sp
                        )

                        Spacer(modifier = Modifier.height(32.dp))
                    }
                }
            }
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