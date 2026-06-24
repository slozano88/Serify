package com.serify.data.repository

import com.serify.data.local.AiRecommendationEntity
import com.serify.data.local.LocalHistoryMapper
import com.serify.data.local.SerifyDao
import com.serify.data.model.Serie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalHistoryRepository @Inject constructor(
    private val dao: SerifyDao
) {

    fun observeRecentlyViewed(userId: String): Flow<List<Serie>> {
        return dao.observeRecentlyViewed(userId, limit = 10).map { items ->
            items.map(LocalHistoryMapper::toSerie)
        }
    }

    suspend fun saveRecentlyViewed(userId: String, serie: Serie) {
        dao.saveRecentlyViewed(
            LocalHistoryMapper.toEntity(
                userId = userId,
                serie = serie,
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

}
