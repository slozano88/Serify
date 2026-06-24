package com.serify.components.saved

import com.serify.data.model.Serie
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SavedScreenStateTest {

    private val drama = serie(
        id = 1,
        name = "Dark",
        genres = listOf("Drama", "Ciencia ficción")
    )
    private val comedy = serie(
        id = 2,
        name = "The Office",
        genres = listOf("Comedia")
    )

    @Test
    fun filteredSeries_returnsAllWhenQueryIsBlank() {
        val state = SavedScreenState(
            savedSeries = listOf(drama, comedy),
            searchQuery = " "
        )

        assertEquals(listOf(drama, comedy), state.filteredSeries)
    }

    @Test
    fun filteredSeries_filtersByNameIgnoringCase() {
        val state = SavedScreenState(
            savedSeries = listOf(drama, comedy),
            searchQuery = "office"
        )

        assertEquals(listOf(comedy), state.filteredSeries)
    }

    @Test
    fun filteredSeries_filtersByGenreIgnoringCase() {
        val state = SavedScreenState(
            savedSeries = listOf(drama, comedy),
            searchQuery = "CIENCIA"
        )

        assertEquals(listOf(drama), state.filteredSeries)
    }

    @Test
    fun filteredSeries_returnsEmptyWhenNothingMatches() {
        val state = SavedScreenState(
            savedSeries = listOf(drama, comedy),
            searchQuery = "terror"
        )

        assertTrue(state.filteredSeries.isEmpty())
    }

    private fun serie(id: Int, name: String, genres: List<String>): Serie {
        return Serie(
            id = id,
            name = name,
            summary = null,
            imageUrl = null,
            rating = null,
            genres = genres,
            premiered = null,
            status = null
        )
    }
}
