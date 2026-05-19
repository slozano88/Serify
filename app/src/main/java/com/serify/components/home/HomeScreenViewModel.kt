package com.serify.components.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.serify.data.repository.SeriesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeScreenViewModel : ViewModel() {

    private val repository = SeriesRepository()

    private val _state = MutableStateFlow(HomeScreenState(isLoading = true))
    val state: StateFlow<HomeScreenState> = _state.asStateFlow()

    init {
        loadHomeSeries()
    }

    private fun loadHomeSeries() {
        viewModelScope.launch {
            try {
                _state.value = HomeScreenState(isLoading = true)

                val series = repository.searchSeries("drama")

                _state.value = HomeScreenState(
                    isLoading = false,
                    featuredSerie = series.firstOrNull(),
                    trendingSeries = series.drop(1).take(3)
                )

            } catch (e: Exception) {
                _state.value = HomeScreenState(
                    isLoading = false,
                    error = "No se pudieron cargar las series."
                )
            }
        }
    }
}