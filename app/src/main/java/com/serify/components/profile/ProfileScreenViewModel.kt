package com.serify.components.profile

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ProfileScreenViewModel : ViewModel() {

    private val _state = MutableStateFlow(ProfileScreenState())
    val state: StateFlow<ProfileScreenState> = _state.asStateFlow()

    /*
    Cuando implemente firebase agrego:

    private val auth = FirebaseAuth.getInstance()

    init {
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
    */
}