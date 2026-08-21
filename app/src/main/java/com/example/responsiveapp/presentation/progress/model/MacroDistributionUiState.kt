package com.example.responsiveapp.presentation.progress.model

import androidx.compose.runtime.Immutable

@Immutable
data class MacroDistributionUiState(
    val hasData: Boolean = false,
    val protein: MacroShareUiState = MacroShareUiState(),
    val carbs: MacroShareUiState = MacroShareUiState(),
    val fats: MacroShareUiState = MacroShareUiState(),
)

@Immutable
data class MacroShareUiState(
    val grams: Int = 0,
    val percentOfCalories: Int = 0,
    val calorieShare: Float = 0f,
)