package com.example.responsiveapp.presentation.home.model

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

@Immutable
data class CaloriesUiModel(
    val consumedText: String = "0",
    val targetText: String = "/ 0 kcal",
    val remainingText: String = "0 kcal left",
    val progress: Float = 0f,
)

@Immutable
data class MacroProgressUiModel(
    val key: String,
    val icon: ImageVector,
    val label: String,
    val consumedText: String,
    val targetText: String,
    val remainingText: String,
    val progress: Float,
    val accentColor: Color,
)

@Immutable
data class NutritionUiState(
    val calories: CaloriesUiModel = CaloriesUiModel(),
    val macros: List<MacroProgressUiModel> = emptyList(),
)
