package com.serify.components.seriesdetail.model

data class Episode(
    val id: Int,
    val name: String,
    val season: Int,
    val number: Int?,
    val summary: String?
)