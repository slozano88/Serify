package com.serify.components.ai

data class AiChatScreenState(
    val messages: List<AiMessage> = listOf(
        AiMessage(
            text = "Hola, soy el chat IA online de Serify. Puedo recomendarte series, comparar géneros y usar tu lista guardada como contexto. Tus últimas recomendaciones quedan disponibles en este dispositivo.",
            isUser = false
        )
    ),
    val input: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)
