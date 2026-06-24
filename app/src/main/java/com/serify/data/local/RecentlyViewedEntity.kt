package com.serify.data.local

import androidx.room.Entity

@Entity(
    tableName = "recently_viewed",
    primaryKeys = ["userId", "serieId"]
)
data class RecentlyViewedEntity(
    val userId: String,
    val serieId: Int,
    val name: String,
    val summary: String?,
    val imageUrl: String?,
    val rating: Double?,
    val genres: String,
    val premiered: String?,
    val status: String?,
    val viewedAt: Long
)
