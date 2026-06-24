package com.serify.components.seriesdetail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.serify.components.seriesdetail.model.Episode
import com.serify.data.model.Serie
import com.serify.data.repository.EnglishToSpanishTranslationRepository
import com.serify.data.repository.FirebaseSeriesRepository
import com.serify.data.repository.LocalHistoryRepository
import com.serify.data.repository.SeriesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SeriesDetailScreenViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = SeriesRepository()
    private val firebaseRepository = FirebaseSeriesRepository()
    private val localHistoryRepository = LocalHistoryRepository(application)
    private val auth = FirebaseAuth.getInstance()

    private val _state = MutableStateFlow(SeriesDetailScreenState())
    val state: StateFlow<SeriesDetailScreenState> = _state.asStateFlow()

    fun loadSerieById(id: Int) {
        viewModelScope.launch {
            try {
                _state.value = SeriesDetailScreenState(isLoading = true)

                val serie = repository.getSerieById(id)
                val cast = repository.getSerieCast(id)
                val seasons = repository.getSeasons(id)
                val episodes = repository.getEpisodes(id)
                val isSaved = firebaseRepository.isSerieSaved(id)

                _state.value = SeriesDetailScreenState(
                    isLoading = false,
                    serie = serie,
                    cast = cast,
                    seasons = seasons,
                    episodes = episodes,
                    isSaved = isSaved
                )

                auth.currentUser?.uid?.let { userId ->
                    try {
                        localHistoryRepository.saveRecentlyViewed(userId, serie)
                    } catch (_: Exception) {
                    }
                }

                translateSerieContent(serie)

            } catch (e: Exception) {
                _state.value = SeriesDetailScreenState(
                    isLoading = false,
                    error = "No se pudo cargar el detalle de la serie."
                )
            }
        }
    }

    private fun translateSerieContent(serie: Serie) {
        viewModelScope.launch {
            val translatedSummary = EnglishToSpanishTranslationRepository.translate(serie.summary)

            if (!translatedSummary.isNullOrBlank()) {
                _state.value = _state.value.copy(
                    serie = _state.value.serie?.copy(summary = translatedSummary)
                )
            }
        }
    }

    fun toggleSaved() {
        val serie = _state.value.serie ?: return

        viewModelScope.launch {
            try {
                _state.value = _state.value.copy(
                    isSaving = true,
                    saveMessage = null
                )

                val newSavedState = firebaseRepository.toggleSavedSerie(serie)

                _state.value = _state.value.copy(
                    isSaved = newSavedState,
                    isSaving = false,
                    saveMessage = if (newSavedState) {
                        "Serie guardada en Mi lista."
                    } else {
                        "Serie eliminada de Mi lista."
                    }
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isSaving = false,
                    saveMessage = "No se pudo actualizar Mi lista."
                )
            }
        }
    }

    fun selectSeason(seasonNumber: Int) {
        val currentSelectedSeason = _state.value.selectedSeason
        val newSelectedSeason = if (currentSelectedSeason == seasonNumber) null else seasonNumber

        _state.value = _state.value.copy(selectedSeason = newSelectedSeason)

        if (newSelectedSeason != null) {
            translateEpisodesForSeason(newSelectedSeason)
        }
    }

    private fun translateEpisodesForSeason(seasonNumber: Int) {
        if (_state.value.translatedSeasonNumbers.contains(seasonNumber)) return

        viewModelScope.launch {
            try {
                _state.value = _state.value.copy(isTranslatingEpisodes = true)

                val currentEpisodes = _state.value.episodes
                val translatedEpisodes = currentEpisodes.map { episode ->
                    if (episode.season == seasonNumber) {
                        episode.toSpanish()
                    } else {
                        episode
                    }
                }

                _state.value = _state.value.copy(
                    episodes = translatedEpisodes,
                    isTranslatingEpisodes = false,
                    translatedSeasonNumbers = _state.value.translatedSeasonNumbers + seasonNumber
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(isTranslatingEpisodes = false)
            }
        }
    }

    private suspend fun Episode.toSpanish(): Episode {
        val translatedName = EnglishToSpanishTranslationRepository.translate(name)
        val translatedSummary = EnglishToSpanishTranslationRepository.translate(summary)

        return copy(
            name = translatedName.takeUnless { it.isNullOrBlank() } ?: name,
            summary = translatedSummary.takeUnless { it.isNullOrBlank() } ?: summary
        )
    }
}
