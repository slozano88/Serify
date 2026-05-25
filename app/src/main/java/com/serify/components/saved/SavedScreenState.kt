package com.serify.components.saved

import com.serify.data.model.Serie

data class SavedScreenState(
    val isLoading: Boolean = false,
    val savedSeries: List<Serie> = emptyList(),
    val searchQuery: String = "",
    val error: String? = null
) {
    val filteredSeries: List<Serie>
        get() = if (searchQuery.isBlank()) {
            savedSeries
        } else {
            savedSeries.filter { serie ->
                serie.name.contains(searchQuery, ignoreCase = true) ||
                        serie.genres.any { genre -> genre.contains(searchQuery, ignoreCase = true) }
            }
        }
}
