package com.macrivo.app.presentation.signin

sealed class SignInEvent {
    object NavigateToMainScreen : SignInEvent()
    object NavigateToUserSetup : SignInEvent()
    data class ShowError(val message: String) : SignInEvent()
    object NavigateToSignUp : SignInEvent()
    data class NavigateToForgotPassword(val email: String) : SignInEvent()
}
