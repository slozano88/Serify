package com.serify.components.home

import com.serify.data.model.Serie

data class HomeScreenState(
    val isLoading: Boolean = false,
    val featuredSerie: Serie? = null,
    val trendingSeries: List<Serie> = emptyList(),
    val error: String? = null
)