package com.serify.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ai_recommendations")
data class AiRecommendationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val userId: String,
    val question: String,
    val answer: String,
    val createdAt: Long
)
