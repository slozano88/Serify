package com.serify.components.login

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class LoginState(
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = false,
    val error: String? = null
)

class LoginScreenViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()

    private val _state = MutableStateFlow(
        LoginState(
            isLoggedIn = auth.currentUser != null
        )
    )
    val state: StateFlow<LoginState> = _state.asStateFlow()

    fun setLoading(value: Boolean) {
        _state.value = _state.value.copy(
            isLoading = value,
            error = null
        )
    }

    fun signInWithGoogleToken(idToken: String?) {
        if (idToken.isNullOrBlank()) {
            _state.value = _state.value.copy(
                isLoading = false,
                error = "No se pudo obtener el token de Google."
            )
            return
        }

        _state.value = _state.value.copy(
            isLoading = true,
            error = null
        )

        val credential = GoogleAuthProvider.getCredential(idToken, null)

        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        isLoggedIn = true,
                        error = null
                    )
                } else {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = task.exception?.message ?: "No se pudo iniciar sesión."
                    )
                }
            }
    }

    fun showError(message: String) {
        _state.value = _state.value.copy(
            isLoading = false,
            error = message
        )
    }

    fun clearError() {
        _state.value = _state.value.copy(error = null)
    }

    fun signOut() {
        auth.signOut()
        _state.value = LoginState()
    }
}