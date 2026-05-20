package com.serify.components.seriesdetail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.serify.components.seriesdetail.model.Season

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
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.surfaceVariant
            }
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Temporada ${season.number ?: "-"}",
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = "Episodios: ${season.episodeOrder ?: "No informado"}",
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = "Estreno: ${season.premiereDate ?: "Sin fecha"}",
                style = MaterialTheme.typography.bodySmall
            )

            Text(
                text = "Finalización: ${season.endDate ?: "Sin fecha"}",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}