package com.example.responsiveapp.presentation.signin_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.responsiveapp.data.datastore.AppPrefManager
import com.example.responsiveapp.domain.use_case.SignInUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val signInUseCase: SignInUseCase,
    private val appPrefManager: AppPrefManager
): ViewModel(){
    private val _state = MutableStateFlow<SignInState>(SignInState.SignOut)
    val state = _state.asStateFlow()
    private val _uiState = MutableStateFlow(SignInUiState())
    val uiState = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<SignInEvent>()
    val event = _event.asSharedFlow()

    init {
    Log.d("LOG","Initialized")
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("LOG","Cleared")
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

    fun signIn() {

        val currentUiState= _uiState.value

        val emailError =
            if (currentUiState.email.isBlank()) "Email required"
            else null

        val passwordError =
            if (currentUiState.password.length < 6) "Password too short"
            else null

        if (emailError != null || passwordError != null) {
            _uiState.update {
                it.copy(emailError = emailError, passwordError = passwordError)
            }
            return
        }

        _state.value = SignInState.Loading
        viewModelScope.launch {
            signInUseCase(currentUiState.email, currentUiState.password).collect { result ->

                result.onSuccess { authResult ->
                    authResult.user?.let {
                        Log.d("LOG",it.toString())
                        appPrefManager.setLoggedIn(true)
                        _state.value = SignInState.SignOut
                        _event.emit(SignInEvent.NavigateToMainScreen)
                    } ?: run {
                        Log.d("LOG",result.isSuccess.toString())
                        _state.value = SignInState.SignOut
                        _event.emit(SignInEvent.ShowError("Something went wrong"))
                    }
                }

                result.onFailure {
                    Log.d("LOG",it.toString())
                    _state.value = SignInState.SignOut
                    _event.emit(SignInEvent.ShowError(it.message ?: "Something went wrong"))
                }
            }
        }
    }

    fun onSignUpClicked() {
        viewModelScope.launch {
            _event.emit(SignInEvent.NavigateToSignUp)
        }
    }
}