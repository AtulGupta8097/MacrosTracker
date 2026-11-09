package com.macrivo.app.presentation.home

import androidx.compose.runtime.Immutable
import com.macrivo.app.domain.model.health.BmiStatus

@Immutable
data class HealthMetricsUiState(
    val bmi: Float? = null,
    val bmiStatus: BmiStatus = BmiStatus.UNKNOWN,
    val tdee: Int? = null,
    val bmr: Int? = null,
)