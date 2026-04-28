package com.example.responsiveapp.presentation.signin

sealed class SignInState {
    object SignOut : SignInState()
    object Loading : SignInState()
}