package com.serify.data.util

object SpanishTextMapper {

    fun genre(value: String): String {
        return when (value.trim().lowercase()) {
            "action" -> "Acción"
            "adventure" -> "Aventura"
            "anime" -> "Anime"
            "children" -> "Infantil"
            "comedy" -> "Comedia"
            "crime" -> "Crimen"
            "diy" -> "Hazlo tú mismo"
            "drama" -> "Drama"
            "family" -> "Familiar"
            "fantasy" -> "Fantasía"
            "food" -> "Gastronomía"
            "history" -> "Historia"
            "horror" -> "Terror"
            "legal" -> "Legal"
            "medical" -> "Medicina"
            "music" -> "Música"
            "mystery" -> "Misterio"
            "nature" -> "Naturaleza"
            "romance" -> "Romance"
            "science-fiction", "sci-fi", "science fiction" -> "Ciencia ficción"
            "sports" -> "Deportes"
            "supernatural" -> "Sobrenatural"
            "thriller" -> "Suspenso"
            "travel" -> "Viajes"
            "war" -> "Bélica"
            "western" -> "Western"
            else -> value
        }
    }

    fun status(value: String?): String? {
        return when (value?.trim()?.lowercase()) {
            null, "" -> value
            "running" -> "En emisión"
            "ended" -> "Finalizada"
            "to be determined" -> "Por determinar"
            "in development" -> "En desarrollo"
            else -> value
        }
    }

    fun genreForDisplay(value: String): String {
        return if (value.equals("Todos", ignoreCase = true)) {
            "Todos"
        } else {
            genre(value)
        }
    }
}
