package com.serify.components.seriesdetail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import com.serify.components.seriesdetail.model.Episode
import com.serify.ui.theme.AppColors

@Composable
fun EpisodeCard(
    episode: Episode
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = AppColors.Surface
        ),
        border = BorderStroke(
            width = 1.dp,
            color = AppColors.Border
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Episodio ${episode.number ?: "-"}",
                color = AppColors.PrimaryBlue,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = episode.name,
                color = AppColors.TextPrimary,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = cleanHtml(episode.summary),
                color = AppColors.TextSecondary,
                fontSize = 13.sp,
                lineHeight = 19.sp
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
        ?: "Sin sinopsis disponible"
}
