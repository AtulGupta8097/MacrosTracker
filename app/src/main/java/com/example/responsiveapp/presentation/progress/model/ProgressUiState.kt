package com.example.responsiveapp.presentation.progress.model

import androidx.compose.runtime.Immutable

@Immutable
data class ProgressUiState(
    val isLoading: Boolean = true,
    val range: ProgressRange = ProgressRange.WEEK,
    val nutritionOverview: NutritionOverviewUiState = NutritionOverviewUiState(),
)