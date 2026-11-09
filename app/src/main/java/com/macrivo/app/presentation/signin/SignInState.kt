package com.macrivo.app.presentation.signin

sealed class SignInState {
    object SignOut : SignInState()
    object Loading : SignInState()
}