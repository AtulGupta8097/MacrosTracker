package com.example.responsiveapp.presentation.home

import androidx.compose.runtime.Immutable
import com.example.responsiveapp.presentation.home.model.AppBarUiState
import com.example.responsiveapp.presentation.home.model.DatePickerUiState
import com.example.responsiveapp.presentation.home.model.HealthMetricsUiState
import com.example.responsiveapp.presentation.home.model.InsightsUiState
import com.example.responsiveapp.presentation.home.model.NutritionUiState
import com.example.responsiveapp.presentation.home.model.RecentMealsUiState

@Immutable
data class HomeUiState(
    val isLoading: Boolean = true,
    val appBar: AppBarUiState = AppBarUiState(),
    val datePicker: DatePickerUiState = DatePickerUiState(),
    val nutrition: NutritionUiState = NutritionUiState(),
    val healthMetrics: HealthMetricsUiState = HealthMetricsUiState(),
    val recentMeals: RecentMealsUiState = RecentMealsUiState(),
    val insights: InsightsUiState = InsightsUiState(),
)
