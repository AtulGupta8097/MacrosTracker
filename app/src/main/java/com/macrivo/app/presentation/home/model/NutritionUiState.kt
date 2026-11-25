package com.macrivo.app.presentation.home.model

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

@Immutable
data class CaloriesUiModel(
    // CHANGED: numeric target, the value the edit sheet starts from
    val targetValue: Int = 0,
    val consumedText: String = "0",
    val targetText: String = "/ 0 kcal",
    val remainingText: String = "0 kcal left",
    val progress: Float = 0f,
)

@Immutable
data class MacroProgressUiModel(
    val key: String,
    val targetValue: Int = 0,
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
    // CHANGED: targets can only be edited while viewing today (past days keep the target they were lived with)
    val canEdit: Boolean = false,
)
