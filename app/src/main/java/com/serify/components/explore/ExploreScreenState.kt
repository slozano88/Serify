package com.serify.components.explore

import com.serify.data.model.Serie

data class ExploreScreenState(
    val isLoading: Boolean = false,
    val searchQuery: String = "",
    val selectedGenre: String = "Todos",
    val series: List<Serie> = emptyList(),
    val error: String? = null,
    val savedSerieIds: Set<Int> = emptySet()
)
