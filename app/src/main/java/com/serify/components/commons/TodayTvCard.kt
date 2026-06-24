package com.serify.components.commons

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.serify.data.model.TodayTvItem
import com.serify.ui.theme.AppColors

@Composable
fun TodayTvCard(
    item: TodayTvItem,
    isSaved: Boolean = false,
    onClick: () -> Unit = {},
    onSaveClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .width(136.dp)
            .height(180.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = AppColors.Surface
        ),
        border = BorderStroke(
            width = 1.dp,
            color = AppColors.Border
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(94.dp)
            ) {
                AsyncImage(
                    model = item.imageUrl,
                    contentDescription = item.showName,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(
                            RoundedCornerShape(
                                topStart = 14.dp,
                                topEnd = 14.dp
                            )
                        )
                        .background(AppColors.SurfaceSoft),
                    contentScale = ContentScale.Crop
                )

                if (item.showId != null) {
                    IconButton(
                        onClick = onSaveClick,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(6.dp)
                            .size(30.dp)
                            .clip(CircleShape)
                            .background(Color(0xCC0B1020))
                    ) {
                        Icon(
                            imageVector = if (isSaved) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                            contentDescription = if (isSaved) "Eliminar de guardadas" else "Guardar serie",
                            tint = if (isSaved) AppColors.AccentBlue else AppColors.TextPrimary,
                            modifier = Modifier.size(17.dp)
                        )
                    }
                }
            }

            Column(
                modifier = Modifier.padding(horizontal = 9.dp, vertical = 8.dp)
            ) {
                Text(
                    text = item.showName,
                    color = AppColors.TextPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = item.episodeName,
                    color = AppColors.TextSecondary,
                    fontSize = 11.sp,
                    maxLines = 1
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = buildString {
                        if (item.airtime != null) append(item.airtime)
                        if (item.season != null && item.number != null) {
                            if (isNotEmpty()) append(" · ")
                            append("T${item.season} E${item.number}")
                        }
                    },
                    color = AppColors.AccentBlue,
                    fontSize = 10.sp,
                    maxLines = 1
                )
            }
        }
    }
}
