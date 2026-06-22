package com.serify.data.model

import com.google.gson.annotations.SerializedName

data class GroqChatRequest(
    val model: String,
    val messages: List<GroqMessage>,
    val temperature: Double = 0.7,
    @SerializedName("max_tokens")
    val maxTokens: Int = 700
)

data class GroqMessage(
    val role: String,
    val content: String
)

data class GroqChatResponse(
    val choices: List<GroqChoice>? = null,
    val error: GroqError? = null
)

data class GroqChoice(
    val message: GroqMessage? = null,
    @SerializedName("finish_reason")
    val finishReason: String? = null
)

data class GroqError(
    val message: String? = null,
    val type: String? = null,
    val code: String? = null
)
