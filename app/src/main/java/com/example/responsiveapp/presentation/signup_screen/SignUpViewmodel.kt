package com.example.responsiveapp.presentation.signup_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.responsiveapp.data.local.AppPrefManager
import com.example.responsiveapp.domain.use_case.SignUpUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class SignUpViewmodel @Inject constructor(
    private val SignUpUseCase: SignUpUseCase,
    private val appPrefManager: AppPrefManager
) : ViewModel() {
    private val _state = MutableStateFlow<SignUpState>(SignUpState.SignOut)
    val state = _state.asStateFlow()

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
        _uiState.update { it.copy(password = value, passwordError = null) }
    }

    fun togglePasswordVisibility() {
        _uiState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
    }
    init {
        Log.d("Log","SignInViewModel Initialized")
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("Log","SignInViewModel Cleared")
    }

    fun signUp() {

        val currentUiState= _uiState.value

        val emailError =
            if (currentUiState.email.isBlank()) "Email required"
            else null

        val passwordError =
            if (currentUiState.password.length < 6) "Password too short"
            else null
        val nameError =
            if (currentUiState.username.isBlank()) "Name required"
            else null

        if (emailError != null || passwordError != null || nameError != null) {
            _uiState.update {
                it.copy(emailError = emailError, passwordError = passwordError, usernameError = nameError)
            }
            return
        }

        _state.value = SignUpState.Loading
        viewModelScope.launch {
            SignUpUseCase(currentUiState.email, currentUiState.password).collect { result ->

                result.onSuccess { authResult ->
                    authResult.user?.let {
                        Log.d("LOG",it.toString())
                        appPrefManager.setLoggedIn(true)
                        _state.value = SignUpState.SignOut
                        _event.emit(SignUpEvent.NavigateToUserSetup)
                    } ?: run {
                        Log.d("LOG",result.isSuccess.toString())
                        _state.value = SignUpState.SignOut
                        _event.emit(SignUpEvent.ShowError("Something went wrong"))
                    }
                }

                result.onFailure {
                    Log.d("LOG",it.toString())
                    _state.value = SignUpState.SignOut
                    _event.emit(SignUpEvent.ShowError(it.message ?: "Something went wrong"))
                }
            }
        }
    }

    fun onSignInClicked() {
        viewModelScope.launch {
            _event.emit(SignUpEvent.NavigateToLogin)
        }
    }

}
