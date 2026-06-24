package com.serify.data.repository

import android.content.Context
import com.serify.data.local.AiRecommendationEntity
import com.serify.data.local.LocalHistoryMapper
import com.serify.data.local.SerifyDatabase
import com.serify.data.model.Serie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocalHistoryRepository(context: Context) {

    private val dao = SerifyDatabase.getInstance(context).serifyDao()

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
