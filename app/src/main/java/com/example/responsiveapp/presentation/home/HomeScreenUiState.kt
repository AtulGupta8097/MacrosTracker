package com.example.responsiveapp.presentation.home

import androidx.compose.runtime.Immutable
import com.example.responsiveapp.domain.model.DailySummary
import com.example.responsiveapp.domain.model.UserProfile
import com.example.responsiveapp.domain.model.foodlog.FoodLog

@Immutable
data class HomeUiState(
    val userProfile: UserProfile? = null,
    val selectedDate: Long = 0L,
    val weekStartDate: Long = 0L,
    val weekDays: List<Long> = emptyList(),
    val dailySummary: DailySummary? = null,
    val foodLogs: List<FoodLog> = emptyList(),
    val isLoading: Boolean = true,
)
