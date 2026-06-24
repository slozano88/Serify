package com.serify.data.local

import com.serify.data.model.Serie
import org.junit.Assert.assertEquals
import org.junit.Test

class LocalHistoryMapperTest {

    @Test
    fun toEntity_preservesSerieAndUserData() {
        val serie = sampleSerie()

        val entity = LocalHistoryMapper.toEntity(
            userId = "firebase-user-1",
            serie = serie,
            viewedAt = 1234L
        )

        assertEquals("firebase-user-1", entity.userId)
        assertEquals(serie.id, entity.serieId)
        assertEquals("Drama|||Misterio", entity.genres)
        assertEquals(1234L, entity.viewedAt)
    }

    @Test
    fun toSerie_restoresGenresAndNullableFields() {
        val original = sampleSerie()
        val entity = LocalHistoryMapper.toEntity(
            userId = "user",
            serie = original,
            viewedAt = 99L
        )

        assertEquals(original, LocalHistoryMapper.toSerie(entity))
    }

    @Test
    fun toSerie_restoresEmptyGenreList() {
        val entity = LocalHistoryMapper.toEntity(
            userId = "user",
            serie = sampleSerie().copy(genres = emptyList()),
            viewedAt = 99L
        )

        assertEquals(emptyList<String>(), LocalHistoryMapper.toSerie(entity).genres)
    }

    private fun sampleSerie(): Serie {
        return Serie(
            id = 42,
            name = "Dark",
            summary = "Una serie de misterio.",
            imageUrl = "https://example.com/dark.jpg",
            rating = 8.8,
            genres = listOf("Drama", "Misterio"),
            premiered = "2017-12-01",
            status = "Finalizada"
        )
    }
}
