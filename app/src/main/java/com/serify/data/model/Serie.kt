package com.serify.data.model

data class Serie(
    val id: Int,
    val name: String,
    val summary: String?,
    val imageUrl: String?,
    val rating: Double?,
    val genres: List<String>,
    val premiered: String?,
    val status: String?
)