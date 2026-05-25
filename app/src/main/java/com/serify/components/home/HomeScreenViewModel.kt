package com.serify.components.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.serify.data.model.Serie
import com.serify.data.repository.FirebaseSeriesRepository
import com.serify.data.repository.SeriesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeScreenViewModel : ViewModel() {

    private val repository = SeriesRepository()
    private val firebaseRepository = FirebaseSeriesRepository()

    private val _state = MutableStateFlow(HomeScreenState(isLoading = true))
    val state: StateFlow<HomeScreenState> = _state.asStateFlow()

    init {
        loadHomeSeries()
    }

    private fun loadHomeSeries() {
        viewModelScope.launch {
            try {
                _state.value = HomeScreenState(isLoading = true)

                val queries = listOf(
                    "love",
                    "war",
                    "school",
                    "crime",
                    "doctor",
                    "family",
                    "space",
                    "king"
                )

                val allSeries = queries
                    .flatMap { query ->
                        repository.searchSeries(query)
                    }
                    .distinctBy { serie ->
                        serie.id
                    }
                    .shuffled()

                val todayTv = try {
                    repository.getTodayTv()
                } catch (e: Exception) {
                    emptyList()
                }

                val savedIds = try {
                    firebaseRepository.getSavedSerieIds()
                } catch (e: Exception) {
                    emptySet()
                }

                _state.value = HomeScreenState(
                    isLoading = false,
                    featuredSerie = allSeries.firstOrNull(),
                    trendingSeries = allSeries.drop(1).take(5),
                    todayTv = todayTv,
                    savedSerieIds = savedIds,
                    error = null
                )

            } catch (e: Exception) {
                _state.value = HomeScreenState(
                    isLoading = false,
                    featuredSerie = null,
                    trendingSeries = emptyList(),
                    todayTv = emptyList(),
                    error = "No se pudieron cargar las series."
                )
            }
        }
    }

    fun toggleSavedById(serieId: Int) {
        viewModelScope.launch {
            try {
                val serie = repository.getSerieById(serieId)
                val isSavedNow = firebaseRepository.toggleSavedSerie(serie)
                val currentIds = _state.value.savedSerieIds

                _state.value = _state.value.copy(
                    savedSerieIds = if (isSavedNow) {
                        currentIds + serieId
                    } else {
                        currentIds - serieId
                    }
                )
            } catch (_: Exception) {
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
