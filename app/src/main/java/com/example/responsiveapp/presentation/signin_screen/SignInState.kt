package com.example.responsiveapp.presentation.signin_screen

sealed class SignInState {
    object SignOut : SignInState()
    object Loading : SignInState()
}