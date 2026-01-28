package com.example.responsiveapp.presentation.signin_screen

sealed class SignInEvent {
    object NavigateToMainScreen : SignInEvent()
    data class ShowError(val message: String) : SignInEvent()
    object NavigateToSignUp : SignInEvent()
}