package com.serify.components.genre

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.serify.data.repository.SeriesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GenreScreenViewModel : ViewModel() {

    private val repository = SeriesRepository()

    private val _state = MutableStateFlow(GenreScreenState())
    val state: StateFlow<GenreScreenState> = _state.asStateFlow()

    fun loadSeriesByGenre(genreName: String) {
        viewModelScope.launch {
            try {
                _state.value = GenreScreenState(
                    isLoading = true,
                    genreName = genreName
                )

                val series = repository.getSeriesByGenre(genreName)

                _state.value = GenreScreenState(
                    isLoading = false,
                    genreName = genreName,
                    series = series
                )

            } catch (e: Exception) {
                _state.value = GenreScreenState(
                    isLoading = false,
                    genreName = genreName,
                    error = "No se pudieron cargar las series de $genreName."
                )
            }
        }
    }
}