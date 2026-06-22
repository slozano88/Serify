package com.serify.components.seriesdetail.model

data class Season(
    val id: Int,
    val number: Int?,
    val episodeOrder: Int?,
    val premiereDate: String?,
    val endDate: String?
)