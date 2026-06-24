package com.serify.data.local

import com.serify.data.model.Serie

object LocalHistoryMapper {

    private const val GENRE_SEPARATOR = "|||"

    fun toEntity(
        userId: String,
        serie: Serie,
        viewedAt: Long
    ): RecentlyViewedEntity {
        return RecentlyViewedEntity(
            userId = userId,
            serieId = serie.id,
            name = serie.name,
            summary = serie.summary,
            imageUrl = serie.imageUrl,
            rating = serie.rating,
            genres = serie.genres.joinToString(GENRE_SEPARATOR),
            premiered = serie.premiered,
            status = serie.status,
            viewedAt = viewedAt
        )
    }

    fun toSerie(entity: RecentlyViewedEntity): Serie {
        return Serie(
            id = entity.serieId,
            name = entity.name,
            summary = entity.summary,
            imageUrl = entity.imageUrl,
            rating = entity.rating,
            genres = entity.genres
                .split(GENRE_SEPARATOR)
                .filter { it.isNotBlank() },
            premiered = entity.premiered,
            status = entity.status
        )
    }
}
