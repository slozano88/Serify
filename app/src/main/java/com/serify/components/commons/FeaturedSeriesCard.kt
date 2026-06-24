package com.serify.components.commons

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.serify.data.model.Serie
import com.serify.ui.theme.AppColors

@Composable
fun FeaturedSeriesCard(
    serie: Serie,
    isSaved: Boolean = false,
    onClick: () -> Unit = {},
    onSaveClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(196.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(AppColors.SurfaceSoft)
            .clickable { onClick() }
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

        Text(
            text = "DESTACADA",
            color = AppColors.TextPrimary,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(start = 12.dp, top = 10.dp)
                .background(AppColors.PrimaryBlue, RoundedCornerShape(20.dp))
                .padding(horizontal = 10.dp, vertical = 4.dp)
        )

        IconButton(
            onClick = onSaveClick,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(end = 10.dp, top = 10.dp)
                .size(36.dp)
                .clip(CircleShape)
                .background(Color(0xCC0B1020))
        ) {
            Icon(
                imageVector = if (isSaved) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                contentDescription = if (isSaved) "Eliminar de guardadas" else "Guardar serie",
                tint = if (isSaved) AppColors.AccentBlue else AppColors.TextPrimary,
                modifier = Modifier.size(20.dp)
            )
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 14.dp, bottom = 16.dp, end = 14.dp)
        ) {
            Text(
                text = serie.name,
                color = AppColors.TextPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                maxLines = 2
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row {
                Text(
                    text = serie.genres.take(2).joinToString(" · "),
                    color = AppColors.TextPrimary.copy(alpha = 0.85f),
                    fontSize = 12.sp
                )

                if (serie.rating != null) {
                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "★ ${serie.rating}",
                        color = AppColors.AccentBlue,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}
