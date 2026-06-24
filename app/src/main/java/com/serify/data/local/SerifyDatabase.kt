package com.serify.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        RecentlyViewedEntity::class,
        AiRecommendationEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class SerifyDatabase : RoomDatabase() {

    abstract fun serifyDao(): SerifyDao
}
