package com.serify.data.repository

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.Translator
import com.serify.data.api.GoogleTranslateApi
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EnglishToSpanishTranslationRepository @Inject constructor(
    private val onlineApi: GoogleTranslateApi,
    private val mlKitTranslator: Translator
) {

    private val modelMutex = Mutex()
    private var isModelReady = false
    private val translationCache = mutableMapOf<String, String>()

    suspend fun translate(text: String?): String? {
        val cleanText = cleanTvMazeText(text)
        if (cleanText.isNullOrBlank()) return cleanText

        translationCache[cleanText]?.let { cachedTranslation ->
            return cachedTranslation
        }

        val translatedText = translateOnline(cleanText)
            ?: translateWithMlKit(cleanText)
            ?: cleanText

        translationCache[cleanText] = translatedText
        return translatedText
    }

    suspend fun translateList(values: List<String>): List<String> {
        return values.map { value ->
            translate(value) ?: value
        }
    }

    private suspend fun translateOnline(text: String): String? {
        return try {
            val chunks = splitText(text)
            val translatedChunks = chunks.map { chunk ->
                parseGoogleTranslateResponse(
                    onlineApi.translateText(text = chunk)
                ) ?: return null
            }

            translatedChunks
                .joinToString(" ")
                .replace("  ", " ")
                .trim()
                .takeIf { it.isNotBlank() }
        } catch (_: Exception) {
            null
        }
    }

    private suspend fun translateWithMlKit(text: String): String? {
        return try {
            ensureModelReady()
            mlKitTranslator.translate(text).await()
        } catch (_: Exception) {
            null
        }
    }

    private suspend fun ensureModelReady() {
        if (isModelReady) return

        modelMutex.withLock {
            if (isModelReady) return@withLock

            val conditions = DownloadConditions.Builder().build()
            mlKitTranslator.downloadModelIfNeeded(conditions).await()
            isModelReady = true
        }
    }

    private fun parseGoogleTranslateResponse(response: JsonElement): String? {
        return try {
            val translatedSegments = response
                .asJsonArray
                .getOrNull(0)
                ?.asJsonArray
                ?.mapNotNull { segment -> segment.asTranslatedTextOrNull() }
                .orEmpty()

            translatedSegments
                .joinToString("")
                .trim()
                .takeIf { it.isNotBlank() }
        } catch (_: Exception) {
            null
        }
    }

    private fun JsonElement.asTranslatedTextOrNull(): String? {
        return try {
            asJsonArray
                .getOrNull(0)
                ?.asString
        } catch (_: Exception) {
            null
        }
    }

    private fun JsonArray.getOrNull(index: Int): JsonElement? {
        return if (index in 0 until size()) get(index) else null
    }

    private fun splitText(text: String): List<String> {
        if (text.length <= 850) return listOf(text)

        val sentences = text
            .replace("\n", " ")
            .split(Regex("(?<=[.!?])\\s+"))
            .filter { it.isNotBlank() }

        if (sentences.isEmpty()) return text.chunked(850)

        val chunks = mutableListOf<String>()
        var currentChunk = ""

        sentences.forEach { sentence ->
            val candidate = if (currentChunk.isBlank()) sentence else "$currentChunk $sentence"

            if (candidate.length > 850) {
                if (currentChunk.isNotBlank()) chunks.add(currentChunk)
                currentChunk = sentence
            } else {
                currentChunk = candidate
            }
        }

        if (currentChunk.isNotBlank()) chunks.add(currentChunk)

        return chunks.flatMap { chunk ->
            if (chunk.length <= 850) listOf(chunk) else chunk.chunked(850)
        }
    }

    fun cleanTvMazeText(text: String?): String? {
        return text
            ?.replace("<br />", "\n")
            ?.replace("<br/>", "\n")
            ?.replace("<br>", "\n")
            ?.replace(Regex("<[^>]*>"), "")
            ?.replace("&amp;", "&")
            ?.replace("&quot;", "\"")
            ?.replace("&#39;", "'")
            ?.replace("&apos;", "'")
            ?.replace("&nbsp;", " ")
            ?.replace("&rsquo;", "’")
            ?.replace("&lsquo;", "‘")
            ?.replace("&rdquo;", "”")
            ?.replace("&ldquo;", "“")
            ?.replace("&ndash;", "–")
            ?.replace("&mdash;", "—")
            ?.replace(Regex("[ \\t]+"), " ")
            ?.trim()
    }
}
