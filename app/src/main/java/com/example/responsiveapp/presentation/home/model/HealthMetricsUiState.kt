package com.example.responsiveapp.presentation.home.model

import androidx.compose.runtime.Immutable
import com.example.responsiveapp.domain.model.health.BmiStatus

@Immutable
data class HealthMetricsUiState(
    val bmiText: String = "—",
    val bmiStatus: BmiStatus = BmiStatus.UNKNOWN,
    val tdeeText: String = "—",
    val bmrText: String = "—",
)
