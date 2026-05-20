package com.serify.components.commons

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.serify.data.model.Serie
import androidx.compose.foundation.clickable

@Composable
fun TrendingSeriesItem(
    ranking: String,
    serie: Serie,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(86.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(9.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = BorderStroke(
            width = 1.dp,
            color = Color(0xFFD7D7D7)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = ranking,
                color = Color(0xFFC9C9C9),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.width(28.dp)
            )

            AsyncImage(
                model = serie.imageUrl,
                contentDescription = serie.name,
                modifier = Modifier
                    .width(36.dp)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color(0xFFD3D3D3)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = serie.name,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF222222)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = buildString {
                        append(serie.genres.take(2).joinToString(" · "))

                        if (serie.rating != null) {
                            if (serie.genres.isNotEmpty()) append(" · ")
                            append("★ ${serie.rating}")
                        }
                    },
                    color = Color(0xFF8A8A8A)
                )
            }

            Icon(
                imageVector = Icons.Outlined.BookmarkBorder,
                contentDescription = "Guardar serie",
                tint = Color(0xFFC7C7C7)
            )
        }
    }
}