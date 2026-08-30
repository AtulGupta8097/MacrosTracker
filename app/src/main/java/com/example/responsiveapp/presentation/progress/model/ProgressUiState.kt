package com.example.responsiveapp.presentation.progress.model

import androidx.compose.runtime.Immutable

@Immutable
data class ProgressUiState(
    val isLoading: Boolean = true,
    val range: ProgressRange = ProgressRange.WEEK,
    val selectedMacro: MacroType = MacroType.CALORIES,
    val nutritionOverview: NutritionOverviewUiState = NutritionOverviewUiState(),
    val macroDistribution: MacroDistributionUiState = MacroDistributionUiState(),
    val weightProgress: WeightProgressUiState = WeightProgressUiState(),
    val isLogWeightDialogVisible: Boolean = false,
)