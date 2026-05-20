package com.serify.components.seriesdetail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.serify.components.seriesdetail.model.Episode

@Composable
fun EpisodeCard(
    episode: Episode
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Episodio ${episode.number ?: "-"}",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                text = episode.name,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = cleanHtml(episode.summary),
                style = MaterialTheme.typography.bodyMedium
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
        ?.replace("<br />", "\n")
        ?: "Sin sinopsis disponible"
}