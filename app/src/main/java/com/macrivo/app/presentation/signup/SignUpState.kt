package com.macrivo.app.presentation.signup
sealed class SignUpState {
    object SignOut : SignUpState()
    object Loading : SignUpState()
}