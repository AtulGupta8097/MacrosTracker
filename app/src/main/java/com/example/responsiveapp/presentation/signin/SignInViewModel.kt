package com.example.responsiveapp.presentation.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.responsiveapp.domain.use_case.authentication.CompleteSignInUseCase
import com.example.responsiveapp.domain.use_case.authentication.SignInUseCase
import com.example.responsiveapp.presentation.auth.AuthValidator
import com.example.responsiveapp.presentation.auth.GoogleSignInHandler
import com.example.responsiveapp.presentation.auth.GoogleSignInOutcome
import com.example.responsiveapp.presentation.auth.GoogleTokenResult
import com.example.responsiveapp.presentation.auth.toUserMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val signInUseCase: SignInUseCase,
    private val completeSignInUseCase: CompleteSignInUseCase,
    private val googleSignInHandler: GoogleSignInHandler
) : ViewModel() {

    private val _uiState = MutableStateFlow(SignInUiState())
    val uiState = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<SignInEvent>()
    val event = _event.asSharedFlow()

    fun onEmailChange(value: String) {
        _uiState.update {
            it.copy(
                email = value,
                emailError = null
            )
        }
    }

    fun onPasswordChange(value: String) {
        _uiState.update {
            it.copy(
                password = value,
                passwordError = null
            )
        }
    }

    fun togglePasswordVisibility() {
        _uiState.update {
            it.copy(
                isPasswordVisible = !it.isPasswordVisible
            )
        }
    }

    fun signIn() {
        val currentUiState = _uiState.value
        if (currentUiState.isBusy) return

        val emailError = AuthValidator.email(currentUiState.email)
        val passwordError = AuthValidator.existingPassword(currentUiState.password)

        if (emailError != null || passwordError != null) {
            _uiState.update {
                it.copy(
                    emailError = emailError,
                    passwordError = passwordError
                )
            }
            return
        }

        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            signInUseCase(
                currentUiState.email.trim(),
                currentUiState.password
            ).first()
                .onSuccess { authResult ->
                    if (authResult.user != null) {
                        val isUserSetup = completeSignInUseCase()
                        _uiState.update { it.copy(isLoading = false) }
                        _event.emit(destination(isUserSetup))
                    } else {
                        _uiState.update { it.copy(isLoading = false) }
                        _event.emit(SignInEvent.ShowError("Something went wrong. Please try again."))
                    }
                }
                .onFailure { throwable ->
                    _uiState.update { it.copy(isLoading = false) }
                    _event.emit(SignInEvent.ShowError(throwable.toUserMessage()))
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
                is GoogleSignInOutcome.Success -> _event.emit(destination(outcome.isUserSetupComplete))
                is GoogleSignInOutcome.Failure -> _event.emit(SignInEvent.ShowError(outcome.message))
                GoogleSignInOutcome.Cancelled -> Unit
            }
        }
    }

    fun onSignUpClicked() {
        if (_uiState.value.isBusy) return
        viewModelScope.launch {
            _event.emit(SignInEvent.NavigateToSignUp)
        }
    }

    fun onForgotPasswordClicked() {
        val current = _uiState.value
        if (current.isBusy) return
        viewModelScope.launch {
            _event.emit(SignInEvent.NavigateToForgotPassword(current.email.trim()))
        }
    }

    private fun destination(isUserSetupComplete: Boolean): SignInEvent =
        if (isUserSetupComplete) SignInEvent.NavigateToMainScreen else SignInEvent.NavigateToUserSetup
}
