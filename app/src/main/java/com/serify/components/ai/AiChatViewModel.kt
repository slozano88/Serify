package com.serify.components.ai

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.serify.data.model.Serie
import com.serify.data.repository.AiRepository
import com.serify.data.repository.FirebaseSeriesRepository
import com.serify.data.repository.LocalHistoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AiChatViewModel @Inject constructor(
    private val firebaseRepository: FirebaseSeriesRepository,
    private val aiRepository: AiRepository,
    private val localHistoryRepository: LocalHistoryRepository,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _state = MutableStateFlow(AiChatScreenState())
    val state: StateFlow<AiChatScreenState> = _state.asStateFlow()

    init {
        loadLatestRecommendations()
    }

    fun onInputChange(value: String) {
        _state.value = _state.value.copy(input = value)
    }

    fun sendMessage() {
        val userMessage = _state.value.input.trim()

        if (userMessage.isBlank() || _state.value.isLoading) return

        viewModelScope.launch {
            _state.value = _state.value.copy(
                messages = _state.value.messages + AiMessage(
                    text = userMessage,
                    isUser = true
                ),
                input = "",
                isLoading = true,
                error = null
            )

            val savedSeries = loadCurrentUserSavedSeriesSafely()

            val answer = aiRepository.askForRecommendation(
                userQuestion = userMessage,
                savedSeries = savedSeries,
                conversation = _state.value.messages
            )

            _state.value = _state.value.copy(
                messages = _state.value.messages + AiMessage(
                    text = answer.text,
                    isUser = false
                ),
                isLoading = false,
                error = null
            )

            val userId = auth.currentUser?.uid
            if (answer.successful && userId != null) {
                try {
                    localHistoryRepository.saveRecommendation(
                        userId = userId,
                        question = userMessage,
                        answer = answer.text
                    )
                } catch (_: Exception) {
                }
            }
        }
    }

    private fun loadLatestRecommendations() {
        val userId = auth.currentUser?.uid ?: return

        viewModelScope.launch {
            try {
                val savedMessages = localHistoryRepository
                    .observeLatestRecommendations(userId)
                    .first()
                    .asReversed()
                    .flatMap { recommendation ->
                        listOf(
                            AiMessage(
                                text = recommendation.question,
                                isUser = true
                            ),
                            AiMessage(
                                text = recommendation.answer,
                                isUser = false
                            )
                        )
                    }

                if (savedMessages.isNotEmpty()) {
                    _state.value = _state.value.copy(
                        messages = _state.value.messages + savedMessages
                    )
                }
            } catch (_: Exception) {
            }
        }
    }

    private suspend fun loadCurrentUserSavedSeriesSafely(): List<Serie> {
        return try {
            firebaseRepository.getSavedSeries()
        } catch (e: Exception) {
            emptyList()
        }
    }
}
