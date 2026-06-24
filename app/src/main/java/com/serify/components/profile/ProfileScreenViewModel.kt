package com.serify.components.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.serify.data.repository.FirebaseSeriesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileScreenViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val firebaseRepository = FirebaseSeriesRepository()

    private val _state = MutableStateFlow(ProfileScreenState())
    val state: StateFlow<ProfileScreenState> = _state.asStateFlow()

    init {
        loadUserData()
    }

    private fun loadUserData() {
        val user = auth.currentUser

        _state.value = ProfileScreenState(
            userName = user?.displayName ?: "Usuario",
            email = user?.email ?: "Sin email",
            photoUrl = user?.photoUrl?.toString(),
            savedCount = 0,
            watchedCount = 0,
            ratingsCount = 0
        )

        viewModelScope.launch {
            try {
                val savedCount = firebaseRepository.getSavedCount()

                _state.value = _state.value.copy(
                    savedCount = savedCount
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    savedCount = 0
                )
            }
        }
    }

    fun signOut() {
        auth.signOut()
    }
}
