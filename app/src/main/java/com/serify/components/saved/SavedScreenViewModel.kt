package com.serify.components.saved

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.serify.data.model.Serie
import com.serify.data.repository.FirebaseSeriesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavedScreenViewModel @Inject constructor(
    private val firebaseRepository: FirebaseSeriesRepository
) : ViewModel() {

    private val _state = MutableStateFlow(SavedScreenState(isLoading = true))
    val state: StateFlow<SavedScreenState> = _state.asStateFlow()

    fun loadSavedSeries() {
        viewModelScope.launch {
            try {
                _state.value = _state.value.copy(
                    isLoading = true,
                    error = null
                )

                val savedSeries = firebaseRepository.getSavedSeries()

                _state.value = _state.value.copy(
                    isLoading = false,
                    savedSeries = savedSeries,
                    error = null
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = "No se pudieron cargar tus series guardadas."
                )
            }
        }
    }

    fun onSearchChange(query: String) {
        _state.value = _state.value.copy(searchQuery = query)
    }

    fun removeSavedSerie(serie: Serie) {
        viewModelScope.launch {
            try {
                firebaseRepository.removeSerie(serie.id)
                _state.value = _state.value.copy(
                    savedSeries = _state.value.savedSeries.filterNot { it.id == serie.id }
                )
            } catch (_: Exception) {
            }
        }
    }
}
