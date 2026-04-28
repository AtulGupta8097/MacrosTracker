package com.example.responsiveapp.presentation.signin

data class SignInUiState (
    val email: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val emailError: String? = null,
    val passwordError: String? = null
)
