package com.example.responsiveapp.presentation.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.responsiveapp.data.datastore.AppPrefManager
import com.example.responsiveapp.domain.use_case.authentication.SignUpUseCase
import com.example.responsiveapp.presentation.auth.AuthValidator
import com.example.responsiveapp.presentation.auth.GoogleSignInHandler
import com.example.responsiveapp.presentation.auth.GoogleSignInOutcome
import com.example.responsiveapp.presentation.auth.GoogleTokenResult
import com.example.responsiveapp.presentation.auth.toUserMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class SignUpViewmodel @Inject constructor(
    private val signUpUseCase: SignUpUseCase,
    private val appPrefManager: AppPrefManager,
    private val googleSignInHandler: GoogleSignInHandler
) : ViewModel() {

    private val _uiState = MutableStateFlow(SignUpUiState())
    val uiState = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<SignUpEvent>()
    val event = _event.asSharedFlow()

    fun onUsernameChange(value: String) {
        _uiState.update { it.copy(username = value, usernameError = null) }
    }

    fun onEmailChange(value: String) {
        _uiState.update { it.copy(email = value, emailError = null) }
    }

    fun onPasswordChange(value: String) {
        _uiState.update { it.copy(password = value, passwordError = null, confirmPasswordError = null) }
    }

    fun onConfirmPasswordChange(value: String) {
        _uiState.update { it.copy(confirmPassword = value, confirmPasswordError = null) }
    }

    fun togglePasswordVisibility() {
        _uiState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
    }

    fun toggleConfirmPasswordVisibility() {
        _uiState.update { it.copy(isConfirmPasswordVisible = !it.isConfirmPasswordVisible) }
    }

    fun signUp() {
        val currentUiState = _uiState.value
        if (currentUiState.isBusy) return

        val nameError = AuthValidator.name(currentUiState.username)
        val emailError = AuthValidator.email(currentUiState.email)
        val passwordError = AuthValidator.newPassword(currentUiState.password)
        val confirmError = AuthValidator.confirmPassword(
            currentUiState.password,
            currentUiState.confirmPassword
        )

        if (nameError != null || emailError != null || passwordError != null || confirmError != null) {
            _uiState.update {
                it.copy(
                    usernameError = nameError,
                    emailError = emailError,
                    passwordError = passwordError,
                    confirmPasswordError = confirmError
                )
            }
            return
        }

        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            signUpUseCase(
                currentUiState.email.trim(),
                currentUiState.password,
                currentUiState.username
            ).first()
                .onSuccess { authResult ->
                    _uiState.update { it.copy(isLoading = false) }
                    if (authResult.user != null) {
                        appPrefManager.setLoggedIn(true)
                        _event.emit(SignUpEvent.NavigateToUserSetup)
                    } else {
                        _event.emit(SignUpEvent.ShowError("Something went wrong. Please try again."))
                    }
                }
                .onFailure { throwable ->
                    _uiState.update { it.copy(isLoading = false) }
                    _event.emit(SignUpEvent.ShowError(throwable.toUserMessage()))
                }
        }
    }

    fun onGoogleSignInClicked(fetchToken: suspend () -> GoogleTokenResult) {
        if (_uiState.value.isBusy) return

        _uiState.update { it.copy(isGoogleLoading = true) }

        viewModelScope.launch {
            val outcome = googleSignInHandler(fetchToken)
            _uiState.update { it.copy(isGoogleLoading = false) }

            when (outcome) {
                is GoogleSignInOutcome.Success -> _event.emit(
                    if (outcome.isUserSetupComplete) SignUpEvent.NavigateToHome
                    else SignUpEvent.NavigateToUserSetup
                )
                is GoogleSignInOutcome.Failure -> _event.emit(SignUpEvent.ShowError(outcome.message))
                GoogleSignInOutcome.Cancelled -> Unit
            }
        }
    }

    fun onSignInClicked() {
        if (_uiState.value.isBusy) return
        viewModelScope.launch {
            _event.emit(SignUpEvent.NavigateToLogin)
        }
    }
}
