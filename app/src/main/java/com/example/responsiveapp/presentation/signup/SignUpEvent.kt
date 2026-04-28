package com.example.responsiveapp.presentation.signup

sealed class SignUpEvent {
    object NavigateToHome : SignUpEvent()
    data class ShowError(val message: String) : SignUpEvent()
    object NavigateToLogin : SignUpEvent()
    object NavigateToUserSetup : SignUpEvent()
}
