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

@Immutable
data class DatePickerUiState(
    val days: List<DateItemUiModel> = emptyList(),
    val isCurrentWeek: Boolean = true,
)
