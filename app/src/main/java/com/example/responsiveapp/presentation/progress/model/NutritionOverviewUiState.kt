package com.example.responsiveapp.presentation.progress.model

import androidx.compose.runtime.Immutable

@Immutable
data class NutritionOverviewUiState(
    val selectedMacro: MacroType = MacroType.PROTEIN,
    val average: NutritionAverageUiState = NutritionAverageUiState(),
    val chartData: List<MacroChartPointUi> = emptyList(),
)

@Immutable
data class MacroChartPointUi(
    val label: String,
    val consumed: Float,
    val target: Float,
)

@Immutable
data class NutritionAverageUiState(
    val calories: Int = 0,
    val protein: Int = 0,
    val carbs: Int = 0,
    val fats: Int = 0,
)