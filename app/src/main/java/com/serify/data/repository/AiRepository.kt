package com.serify.data.repository

import com.serify.components.ai.AiMessage
import com.serify.data.api.AiApi
import com.serify.data.model.Serie
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class AiRepository @Inject constructor(
    private val api: AiApi,
    @Named("groqApiKey") private val apiKey: String,
    @Named("groqModel") private val model: String
) {

    suspend fun askForRecommendation(
        userQuestion: String,
        savedSeries: List<Serie>,
        conversation: List<AiMessage>
    ): AiResult {
        if (apiKey.isBlank() || apiKey == "PEGAR_TU_API_KEY_ACA") {
            return AiResult(
                text = "Falta configurar Groq. Agregá GROQ_API_KEY en local.properties, sincronizá Gradle y volvé a ejecutar la app.",
                successful = false
            )
        }

        val request = AiRequestFactory.create(
            model = model,
            userQuestion = userQuestion,
            savedSeries = savedSeries,
            conversation = conversation
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

    private fun readError(e: HttpException): String {
        return e.response()?.errorBody()?.string()?.takeIf { it.isNotBlank() } ?: "sin detalle"
    }

}

data class AiResult(
    val text: String,
    val successful: Boolean
)
