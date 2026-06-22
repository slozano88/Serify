package com.serify.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SerifyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveRecentlyViewed(item: RecentlyViewedEntity)

    @Query(
        """
        SELECT * FROM recently_viewed
        WHERE userId = :userId
        ORDER BY viewedAt DESC
        LIMIT :limit
        """
    )
    fun observeRecentlyViewed(
        userId: String,
        limit: Int
    ): Flow<List<RecentlyViewedEntity>>

    @Query("DELETE FROM recently_viewed WHERE userId = :userId")
    suspend fun clearRecentlyViewed(userId: String)

    @Insert
    suspend fun saveRecommendation(item: AiRecommendationEntity)

    @Query(
        """
        SELECT * FROM ai_recommendations
        WHERE userId = :userId
        ORDER BY createdAt DESC
        LIMIT :limit
        """
    )
    fun observeLatestRecommendations(
        userId: String,
        limit: Int
    ): Flow<List<AiRecommendationEntity>>

    @Query(
        """
        DELETE FROM ai_recommendations
        WHERE userId = :userId
        AND id NOT IN (
            SELECT id FROM ai_recommendations
            WHERE userId = :userId
            ORDER BY createdAt DESC
            LIMIT :keep
        )
        """
    )
    suspend fun trimRecommendations(userId: String, keep: Int)
}
