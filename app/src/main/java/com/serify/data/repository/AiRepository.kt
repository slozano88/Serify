package com.serify.data.repository

import com.google.gson.Gson
import com.serify.BuildConfig
import com.serify.components.ai.AiMessage
import com.serify.data.api.AiApi
import com.serify.data.model.GroqChatRequest
import com.serify.data.model.GroqMessage
import com.serify.data.model.Serie
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class AiRepository {

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.groq.com/openai/v1/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val api = retrofit.create(AiApi::class.java)
    private val gson = Gson()

    suspend fun askForRecommendation(
        userQuestion: String,
        savedSeries: List<Serie>,
        conversation: List<AiMessage>
    ): AiResult {
        val apiKey = BuildConfig.GROQ_API_KEY.trim()
        val model = BuildConfig.GROQ_MODEL.trim().ifBlank { "llama-3.1-8b-instant" }

        if (apiKey.isBlank() || apiKey == "PEGAR_TU_API_KEY_ACA") {
            return AiResult(
                text = "Falta configurar Groq. Agregá GROQ_API_KEY en local.properties, sincronizá Gradle y volvé a ejecutar la app.",
                successful = false
            )
        }

        val history = conversation
            .filterNot { it.text.startsWith("Hola, soy") }
            .takeLast(8)
            .map { message ->
                GroqMessage(
                    role = if (message.isUser) "user" else "assistant",
                    content = message.text
                )
            }
            .dropLastWhile { it.role == "user" }

        val request = GroqChatRequest(
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

        return try {
            val response = api.createChatCompletion(
                authorization = "Bearer $apiKey",
                request = request
            )

            response.error?.message?.let { errorMessage ->
                return AiResult(
                    text = "Groq respondió con un error: $errorMessage",
                    successful = false
                )
            }

            val answer = response.choices
                ?.firstOrNull()
                ?.message
                ?.content
                ?.trim()
                ?.takeIf { it.isNotBlank() }
                ?: return AiResult(
                    text = "La IA no devolvió una respuesta útil. Probá reformular la consulta.",
                    successful = false
                )

            AiResult(text = answer, successful = true)

        } catch (e: HttpException) {
            val message = when (e.code()) {
                400 -> "Groq rechazó la solicitud. Revisá el modelo configurado. Detalle: ${readError(e)}"
                401 -> "La API key de Groq no es válida. Creá una nueva key y actualizá GROQ_API_KEY."
                403 -> "La API key de Groq no tiene permiso para usar este modelo."
                404 -> "El modelo no está disponible. Usá GROQ_MODEL=llama-3.1-8b-instant."
                429 -> "Se alcanzó temporalmente un límite de Groq. Esperá unos segundos y volvé a intentar."
                else -> "Groq respondió con error ${e.code()}: ${readError(e)}"
            }
            AiResult(text = message, successful = false)
        } catch (e: IOException) {
            AiResult(
                text = "No pude conectarme con Groq. Verificá la conexión a internet del dispositivo.",
                successful = false
            )
        } catch (e: Exception) {
            AiResult(
                text = "Ocurrió un error inesperado al consultar Groq: ${e.message ?: "sin detalle"}",
                successful = false
            )
        }
    }

    private fun buildSystemPrompt(savedSeries: List<Serie>): String {
        val seriesContext = savedSeries
            .take(8)
            .map { serie ->
                mapOf(
                    "name" to serie.name,
                    "genres" to serie.genres,
                    "rating" to serie.rating,
                    "summary" to cleanSummary(serie.summary).take(250)
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
            ${gson.toJson(seriesContext)}
        """.trimIndent()
    }

    private fun readError(e: HttpException): String {
        return e.response()?.errorBody()?.string()?.takeIf { it.isNotBlank() } ?: "sin detalle"
    }

    private fun cleanSummary(summary: String?): String {
        if (summary.isNullOrBlank()) return "Sin sinopsis disponible."

        return summary
            .replace(Regex("<[^>]*>"), "")
            .replace(Regex("\\s+"), " ")
            .trim()
    }

}

data class AiResult(
    val text: String,
    val successful: Boolean
)
