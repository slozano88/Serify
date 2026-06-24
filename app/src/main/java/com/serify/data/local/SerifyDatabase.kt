package com.serify.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
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

    companion object {
        @Volatile
        private var instance: SerifyDatabase? = null

        fun getInstance(context: Context): SerifyDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    SerifyDatabase::class.java,
                    "serify_local.db"
                ).build().also { database ->
                    instance = database
                }
            }
        }
    }
}
