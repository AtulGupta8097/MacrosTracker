package com.example.responsiveapp.presentation.home.model

import androidx.compose.runtime.Immutable

@Immutable
data class StreakDayUiModel(
    val weekdayLabel: String,
    val dayNumber: String,
    val isLogged: Boolean,
    val isToday: Boolean,
    val isFuture: Boolean,
)

@Immutable
data class StreakWeekUiState(
    val currentStreakCount: Int = 0,
    val currentStreakText: String = "0",
    val days: List<StreakDayUiModel> = emptyList(),
)
