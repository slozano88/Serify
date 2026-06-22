package com.serify.components.seriesdetail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.serify.components.seriesdetail.model.Season
import com.serify.ui.theme.AppColors

@Composable
fun SeasonCard(
    season: Season,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) AppColors.SurfaceElevated else AppColors.Surface
        ),
        border = BorderStroke(
            width = 1.dp,
            color = if (isSelected) AppColors.PrimaryBlue else AppColors.Border
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Temporada ${season.number ?: "-"}",
                color = AppColors.TextPrimary,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Episodios: ${season.episodeOrder ?: "No informado"}",
                color = AppColors.TextSecondary,
                fontSize = 13.sp
            )

            Text(
                text = "Estreno: ${season.premiereDate ?: "Sin fecha"}",
                color = AppColors.TextSecondary,
                fontSize = 12.sp
            )

            Text(
                text = "Finalización: ${season.endDate ?: "Sin fecha"}",
                color = AppColors.TextMuted,
                fontSize = 12.sp
            )
        }
    }
}
