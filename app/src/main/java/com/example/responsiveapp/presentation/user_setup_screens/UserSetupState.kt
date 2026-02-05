package com.example.responsiveapp.presentation.user_setup_screens

import com.example.responsiveapp.domain.model.MacroNutrients

data class UserSetupState(
    val currentScreen: UserSetupScreen = UserSetupScreen.Gender,
    val userInput: UserInput = UserInput(),
    val isSaving: Boolean = false,
    val error: String? = null,
    val macros: MacroNutrients? = null
)