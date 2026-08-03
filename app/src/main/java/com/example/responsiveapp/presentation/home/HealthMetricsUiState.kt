package com.example.responsiveapp.presentation.home

import androidx.compose.runtime.Immutable
import com.example.responsiveapp.domain.model.health.BmiStatus

@Immutable
data class HealthMetricsUiState(
    val bmi: Float? = null,
    val bmiStatus: BmiStatus = BmiStatus.UNKNOWN,
    val tdee: Int? = null,
    val bmr: Int? = null,
)