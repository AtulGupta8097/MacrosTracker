package com.example.responsiveapp.presentation.signup_screen
sealed class SignUpState {
    object SignOut : SignUpState()
    object Loading : SignUpState()
}