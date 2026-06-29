package com.example.responsiveapp.presentation.usersetup

import com.example.responsiveapp.domain.model.NutritionTargets

data class UserSetupState(
    val currentScreen: UserSetupScreen = UserSetupScreen.Gender,
    val userInput: UserInput = UserInput(),
    val isSaving: Boolean = false,
    val error: String? = null,
    val macros: NutritionTargets? = null
)