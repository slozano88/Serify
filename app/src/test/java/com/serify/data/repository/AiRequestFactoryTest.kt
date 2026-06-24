package com.serify.data.repository

import com.serify.components.ai.AiMessage
import com.serify.data.model.Serie
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class AiRequestFactoryTest {

    @Test
    fun create_placesSystemFirstAndCurrentQuestionLast() {
        val request = AiRequestFactory.create(
            model = "test-model",
            userQuestion = "¿Qué serie me recomendás?",
            savedSeries = emptyList(),
            conversation = listOf(
                AiMessage("Hola, soy el chat IA online de Serify.", isUser = false),
                AiMessage("Me gustan los dramas", isUser = true),
                AiMessage("Podrías ver Dark", isUser = false),
                AiMessage("¿Qué serie me recomendás?", isUser = true)
            )
        )

        assertEquals("test-model", request.model)
        assertEquals("system", request.messages.first().role)
        assertEquals("user", request.messages.last().role)
        assertEquals("¿Qué serie me recomendás?", request.messages.last().content)
        assertEquals(1, request.messages.count { it.content == "¿Qué serie me recomendás?" })
    }

    @Test
    fun create_limitsSavedSeriesContextToEightItems() {
        val savedSeries = (1..10).map { index ->
            serie(index, "Serie $index", "Resumen $index")
        }

        val request = AiRequestFactory.create(
            model = "test-model",
            userQuestion = "Recomendame algo",
            savedSeries = savedSeries,
            conversation = emptyList()
        )

        val systemPrompt = request.messages.first().content
        assertTrue(systemPrompt.contains("Serie 8"))
        assertFalse(systemPrompt.contains("Serie 9"))
        assertFalse(systemPrompt.contains("Serie 10"))
    }

    @Test
    fun create_cleansHtmlAndLimitsLongSummaries() {
        val summary = "<p>${"a".repeat(400)}</p>"

        val request = AiRequestFactory.create(
            model = "test-model",
            userQuestion = "Contame sobre esta serie",
            savedSeries = listOf(serie(1, "Serie larga", summary)),
            conversation = emptyList()
        )

        val systemPrompt = request.messages.first().content
        assertFalse(systemPrompt.contains("<p>"))
        assertFalse(systemPrompt.contains("a".repeat(251)))
        assertTrue(systemPrompt.contains("a".repeat(250)))
    }

    @Test
    fun create_keepsAtMostEightPreviousMessages() {
        val conversation = (1..12).map { index ->
            AiMessage(
                text = "Mensaje $index",
                isUser = index % 2 != 0
            )
        }

        val request = AiRequestFactory.create(
            model = "test-model",
            userQuestion = "Nueva pregunta",
            savedSeries = emptyList(),
            conversation = conversation
        )

        val history = request.messages.drop(1).dropLast(1)
        assertTrue(history.size <= 8)
        assertFalse(history.any { it.content == "Mensaje 1" })
        assertEquals("Nueva pregunta", request.messages.last().content)
    }

    private fun serie(id: Int, name: String, summary: String): Serie {
        return Serie(
            id = id,
            name = name,
            summary = summary,
            imageUrl = null,
            rating = 8.0,
            genres = listOf("Drama"),
            premiered = null,
            status = null
        )
    }
}
