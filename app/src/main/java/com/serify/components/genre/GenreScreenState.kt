package com.serify.components.genre

import com.serify.data.model.Serie

data class GenreScreenState(
    val isLoading: Boolean = false,
    val genreName: String = "",
    val series: List<Serie> = emptyList(),
    val error: String? = null,
    val savedSerieIds: Set<Int> = emptySet()
)
