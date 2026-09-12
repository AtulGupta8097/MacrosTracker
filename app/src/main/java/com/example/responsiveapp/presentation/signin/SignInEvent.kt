package com.example.responsiveapp.presentation.signin

sealed class SignInEvent {
    object NavigateToMainScreen : SignInEvent()
    object NavigateToUserSetup : SignInEvent()
    data class ShowError(val message: String) : SignInEvent()
    object NavigateToSignUp : SignInEvent()
}