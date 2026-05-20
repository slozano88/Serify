package com.serify.components.profile

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ProfileScreenViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()

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
            watchedCount = 47,
            ratingsCount = 23
        )
    }

    fun signOut() {
        auth.signOut()
    }
}