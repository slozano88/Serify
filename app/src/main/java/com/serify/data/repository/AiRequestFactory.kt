package com.serify.data.repository

import com.google.gson.Gson
import com.serify.components.ai.AiMessage
import com.serify.data.model.GroqChatRequest
import com.serify.data.model.GroqMessage
import com.serify.data.model.Serie

object AiRequestFactory {

    private const val MAX_HISTORY_MESSAGES = 8
    private const val MAX_SAVED_SERIES = 8
    private const val MAX_SUMMARY_LENGTH = 250

    fun create(
        model: String,
        userQuestion: String,
        savedSeries: List<Serie>,
        conversation: List<AiMessage>
    ): GroqChatRequest {
        val history = conversation
            .filterNot { it.text.startsWith("Hola, soy") }
            .takeLast(MAX_HISTORY_MESSAGES)
            .map { message ->
                GroqMessage(
                    role = if (message.isUser) "user" else "assistant",
                    content = message.text
                )
            }
            .dropLastWhile { it.role == "user" }

        return GroqChatRequest(
            model = model,
            messages = listOf(
                GroqMessage(
                    role = "system",
                    content = buildSystemPrompt(savedSeries)
                )
            ) + history + GroqMessage(
                role = "user",
                content = userQuestion
            )
        )
    }

    private fun buildSystemPrompt(savedSeries: List<Serie>): String {
        val seriesContext = savedSeries
            .take(MAX_SAVED_SERIES)
            .map { serie ->
                mapOf(
                    "name" to serie.name,
                    "genres" to serie.genres,
                    "rating" to serie.rating,
                    "summary" to cleanSummary(serie.summary).take(MAX_SUMMARY_LENGTH)
                )
            }

        return """
            Sos el asistente online de Serify, especializado en series.
            - Respondé siempre en español.
            - Contestá sobre series, episodios, temporadas, géneros, actores y recomendaciones.
            - Podés usar tu conocimiento general aunque una serie no esté guardada.
            - Usá la lista guardada solo cuando ayude a personalizar la respuesta.
            - Si el usuario pregunta algo fuera de series, rechazá amablemente y recordá que solo podés ayudar con series.
            - No inventes datos concretos si no estás seguro.
            - Sé claro y conversacional. Priorizá respuestas de menos de 500 palabras.

            Lista guardada del usuario actual:
            ${Gson().toJson(seriesContext)}
        """.trimIndent()
    }

    private fun cleanSummary(summary: String?): String {
        if (summary.isNullOrBlank()) return "Sin sinopsis disponible."

        return summary
            .replace(Regex("<[^>]*>"), "")
            .replace(Regex("\\s+"), " ")
            .trim()
    }
}
