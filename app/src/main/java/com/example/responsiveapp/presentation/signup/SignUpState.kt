package com.example.responsiveapp.presentation.signup
sealed class SignUpState {
    object SignOut : SignUpState()
    object Loading : SignUpState()
}