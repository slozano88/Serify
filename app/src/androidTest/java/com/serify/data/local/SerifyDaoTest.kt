package com.serify.data.local

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SerifyDaoTest {

    private lateinit var database: SerifyDatabase
    private lateinit var dao: SerifyDao

    @Before
    fun createDatabase() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(
            context,
            SerifyDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = database.serifyDao()
    }

    @After
    fun closeDatabase() {
        database.close()
    }

    @Test
    fun recentlyViewed_isOrderedAndIsolatedByUser() = runBlocking {
        dao.saveRecentlyViewed(recent("user-a", 1, viewedAt = 100L))
        dao.saveRecentlyViewed(recent("user-a", 2, viewedAt = 300L))
        dao.saveRecentlyViewed(recent("user-b", 3, viewedAt = 500L))

        val result = dao.observeRecentlyViewed("user-a", limit = 10).first()

        assertEquals(listOf(2, 1), result.map { it.serieId })
    }

    @Test
    fun recentlyViewed_replacesDuplicateSeriesAndUpdatesTimestamp() = runBlocking {
        dao.saveRecentlyViewed(recent("user-a", 1, viewedAt = 100L))
        dao.saveRecentlyViewed(recent("user-a", 1, viewedAt = 900L))

        val result = dao.observeRecentlyViewed("user-a", limit = 10).first()

        assertEquals(1, result.size)
        assertEquals(900L, result.first().viewedAt)
    }

    @Test
    fun recommendations_areLimitedAndTrimmed() = runBlocking {
        (1..5).forEach { index ->
            dao.saveRecommendation(
                AiRecommendationEntity(
                    userId = "user-a",
                    question = "Pregunta $index",
                    answer = "Respuesta $index",
                    createdAt = index.toLong()
                )
            )
        }

        dao.trimRecommendations("user-a", keep = 3)
        val result = dao.observeLatestRecommendations("user-a", limit = 10).first()

        assertEquals(3, result.size)
        assertEquals(listOf("Pregunta 5", "Pregunta 4", "Pregunta 3"), result.map { it.question })
    }

    private fun recent(
        userId: String,
        serieId: Int,
        viewedAt: Long
    ): RecentlyViewedEntity {
        return RecentlyViewedEntity(
            userId = userId,
            serieId = serieId,
            name = "Serie $serieId",
            summary = null,
            imageUrl = null,
            rating = null,
            genres = "",
            premiered = null,
            status = null,
            viewedAt = viewedAt
        )
    }
}
