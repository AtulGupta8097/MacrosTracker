package com.example.responsiveapp.presentation.user_setup_screens

data class UserSetupState(
    val currentScreen: UserSetupScreen = UserSetupScreen.Gender,
    val userInput: UserInput = UserInput()
)