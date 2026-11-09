package com.macrivo.app.presentation.home

import androidx.compose.runtime.Immutable
import com.macrivo.app.presentation.home.model.DatePickerUiState
import com.macrivo.app.presentation.home.model.HealthMetricsUiState
import com.macrivo.app.presentation.home.model.InsightsUiState
import com.macrivo.app.presentation.home.model.NutritionUiState
import com.macrivo.app.presentation.home.model.RecentMealsUiState
import com.macrivo.app.presentation.home.model.StreakWeekUiState
import com.macrivo.app.presentation.home.model.WeekUiState

@Immutable
data class HomeUiState(
    val isLoading: Boolean = true,
    val streak: StreakWeekUiState = StreakWeekUiState(),
    val userName: String = "",
    val datePicker: DatePickerUiState = DatePickerUiState(
        week = WeekUiState(),
    ),
    val nutrition: NutritionUiState = NutritionUiState(),
    val healthMetrics: HealthMetricsUiState = HealthMetricsUiState(),
    val recentMeals: RecentMealsUiState = RecentMealsUiState(),
    val insights: InsightsUiState = InsightsUiState(),
)
