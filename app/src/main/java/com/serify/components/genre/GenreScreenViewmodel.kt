package com.serify.components.genre

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
class GenreScreenViewModel @Inject constructor(
    private val repository: ISeriesRepository,
    private val firebaseRepository: FirebaseSeriesRepository
) : ViewModel() {

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
                val savedIds = try {
                    firebaseRepository.getSavedSerieIds()
                } catch (e: Exception) {
                    emptySet()
                }

                _state.value = GenreScreenState(
                    isLoading = false,
                    genreName = genreName,
                    series = series,
                    savedSerieIds = savedIds
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
