package com.serify.components.explore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.serify.data.model.Serie
import com.serify.data.repository.FirebaseSeriesRepository
import com.serify.domain.ISeriesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExploreScreenViewModel @Inject constructor(
    private val repository: ISeriesRepository,
    private val firebaseRepository: FirebaseSeriesRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ExploreScreenState(isLoading = true))
    val state: StateFlow<ExploreScreenState> = _state.asStateFlow()

    init {
        loadAllSeries()
    }

    fun onSearchChange(query: String) {
        _state.value = _state.value.copy(searchQuery = query)

        if (query.isBlank()) {
            if (_state.value.selectedGenre == "Todos") {
                loadAllSeries()
            } else {
                loadSeriesByGenre(_state.value.selectedGenre)
            }
        } else {
            searchSeries(query)
        }
    }

    fun onGenreSelected(genre: String) {
        _state.value = _state.value.copy(
            selectedGenre = genre,
            searchQuery = ""
        )

        if (genre == "Todos") {
            loadAllSeries()
        } else {
            loadSeriesByGenre(genre)
        }
    }

    private suspend fun loadSavedIds(): Set<Int> {
        return try {
            firebaseRepository.getSavedSerieIds()
        } catch (e: Exception) {
            emptySet()
        }
    }

    private fun loadAllSeries() {
        viewModelScope.launch {
            try {
                _state.value = _state.value.copy(
                    isLoading = true,
                    error = null
                )

                val queries = listOf(
                    "school",
                    "crime",
                    "doctor",
                    "family",
                    "space",
                    "love",
                    "dark",
                    "house"
                )

                val result = queries
                    .flatMap { query ->
                        repository.searchSeries(query)
                    }
                    .distinctBy { it.id }
                    .shuffled()
                    .take(10)

                _state.value = _state.value.copy(
                    isLoading = false,
                    series = result,
                    savedSerieIds = loadSavedIds(),
                    error = null
                )

            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = "No se pudieron cargar las series."
                )
            }
        }
    }

    private fun loadSeriesByGenre(genre: String) {
        viewModelScope.launch {
            try {
                _state.value = _state.value.copy(
                    isLoading = true,
                    error = null
                )

                val result = repository.getSeriesByGenre(genre)

                _state.value = _state.value.copy(
                    isLoading = false,
                    series = result,
                    savedSerieIds = loadSavedIds(),
                    error = null
                )

            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = "No se pudieron cargar las series de $genre."
                )
            }
        }
    }

    private fun searchSeries(query: String) {
        viewModelScope.launch {
            try {
                _state.value = _state.value.copy(
                    isLoading = true,
                    error = null
                )

                val result = repository.searchSeries(query)
                    .distinctBy { it.id }
                    .take(10)

                _state.value = _state.value.copy(
                    isLoading = false,
                    series = result,
                    savedSerieIds = loadSavedIds(),
                    error = null
                )

            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = "No se encontraron resultados."
                )
            }
        }
    }

    fun toggleSaved(serie: Serie) {
        viewModelScope.launch {
            try {
                val isSavedNow = firebaseRepository.toggleSavedSerie(serie)
                val currentIds = _state.value.savedSerieIds

                _state.value = _state.value.copy(
                    savedSerieIds = if (isSavedNow) {
                        currentIds + serie.id
                    } else {
                        currentIds - serie.id
                    }
                )
            } catch (_: Exception) {
            }
        }
    }
}
