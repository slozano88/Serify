package com.serify.components.seriesdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.serify.data.repository.SeriesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SeriesDetailScreenViewModel : ViewModel() {

    private val repository = SeriesRepository()

    private val _state = MutableStateFlow(SeriesDetailScreenState())
    val state: StateFlow<SeriesDetailScreenState> = _state.asStateFlow()

    fun loadSerieById(id: Int) {
        viewModelScope.launch {
            try {
                _state.value = SeriesDetailScreenState(isLoading = true)

                val serie = repository.getSerieById(id)
                val cast = repository.getSerieCast(id)

                _state.value = SeriesDetailScreenState(
                    isLoading = false,
                    serie = serie,
                    cast = cast
                )

            } catch (e: Exception) {
                _state.value = SeriesDetailScreenState(
                    isLoading = false,
                    error = "No se pudo cargar el detalle de la serie."
                )
            }
        }
    }
}