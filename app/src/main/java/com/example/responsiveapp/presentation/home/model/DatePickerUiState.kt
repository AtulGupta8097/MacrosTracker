package com.example.responsiveapp.presentation.home.model

import androidx.compose.runtime.Immutable

@Immutable
data class DateItemUiModel(
    val epochMillis: Long,
    val weekdayLabel: String,
    val dayLabel: String,
    val isSelected: Boolean,
    val isToday: Boolean,
    val isFuture: Boolean,
)

data class WeekUiState(
    val weekStartDate: Long = 0L,
    val days: List<DateItemUiModel> = emptyList(),
)

@Immutable
data class DatePickerUiState(
    val week: WeekUiState,
    val isCurrentWeek: Boolean = true,
)
