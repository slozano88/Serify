package com.serify.data.repository

import android.content.Context
import com.serify.data.local.AiRecommendationEntity
import com.serify.data.local.RecentlyViewedEntity
import com.serify.data.local.SerifyDatabase
import com.serify.data.model.Serie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocalHistoryRepository(context: Context) {

    private val dao = SerifyDatabase.getInstance(context).serifyDao()

    fun observeRecentlyViewed(userId: String): Flow<List<Serie>> {
        return dao.observeRecentlyViewed(userId, limit = 10).map { items ->
            items.map { item -> item.toSerie() }
        }
    }

    suspend fun saveRecentlyViewed(userId: String, serie: Serie) {
        dao.saveRecentlyViewed(
            RecentlyViewedEntity(
                userId = userId,
                serieId = serie.id,
                name = serie.name,
                summary = serie.summary,
                imageUrl = serie.imageUrl,
                rating = serie.rating,
                genres = serie.genres.joinToString(GENRE_SEPARATOR),
                premiered = serie.premiered,
                status = serie.status,
                viewedAt = System.currentTimeMillis()
            )
        )
    }

    fun observeLatestRecommendations(userId: String): Flow<List<AiRecommendationEntity>> {
        return dao.observeLatestRecommendations(userId, limit = 6)
    }

    suspend fun saveRecommendation(
        userId: String,
        question: String,
        answer: String
    ) {
        dao.saveRecommendation(
            AiRecommendationEntity(
                userId = userId,
                question = question,
                answer = answer,
                createdAt = System.currentTimeMillis()
            )
        )
        dao.trimRecommendations(userId, keep = 20)
    }

    private fun RecentlyViewedEntity.toSerie(): Serie {
        return Serie(
            id = serieId,
            name = name,
            summary = summary,
            imageUrl = imageUrl,
            rating = rating,
            genres = genres
                .split(GENRE_SEPARATOR)
                .filter { it.isNotBlank() },
            premiered = premiered,
            status = status
        )
    }

    private companion object {
        const val GENRE_SEPARATOR = "|||"
    }
}
