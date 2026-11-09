package com.macrivo.app.presentation.forgetpassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.macrivo.app.domain.model.auth.AuthFailure
import com.macrivo.app.domain.use_case.authentication.SendPasswordResetEmailUseCase
import com.macrivo.app.presentation.auth.AuthValidator
import com.macrivo.app.presentation.auth.authFailure
import com.macrivo.app.presentation.auth.toUserMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ForgotPasswordUiState(
    val email: String = "",
    val emailError: String? = null,
    val isLoading: Boolean = false,
    val sentTo: String? = null,
    val errorMessage: String? = null,
)

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val sendPasswordResetEmail: SendPasswordResetEmailUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(ForgotPasswordUiState())
    val state: StateFlow<ForgotPasswordUiState> = _state.asStateFlow()

    private var prefilled = false

    fun prefill(email: String) {
        if (prefilled) return
        prefilled = true
        if (email.isNotBlank()) _state.update { it.copy(email = email) }
    }

    fun onEmailChange(value: String) {
        _state.update { it.copy(email = value, emailError = null, errorMessage = null) }
    }

    fun onSendClicked() {
        val current = _state.value
        if (current.isLoading) return

        val emailError = AuthValidator.email(current.email)
        if (emailError != null) {
            _state.update { it.copy(emailError = emailError) }
            return
        }

        _state.update { it.copy(isLoading = true, errorMessage = null) }
        val email = current.email.trim()

        viewModelScope.launch {
            val result = sendPasswordResetEmail(email).first()
            result
                .onSuccess { showSent(email) }
                .onFailure { error ->
                    if (error.authFailure() == AuthFailure.InvalidCredentials) {
                        showSent(email)
                    } else {
                        _state.update {
                            it.copy(isLoading = false, errorMessage = error.toUserMessage())
                        }
                    }
                }
        }
    }

    fun onUseDifferentEmail() {
        _state.update { it.copy(sentTo = null, errorMessage = null) }
    }

    private fun showSent(email: String) {
        _state.update { it.copy(isLoading = false, sentTo = email) }
    }
}
