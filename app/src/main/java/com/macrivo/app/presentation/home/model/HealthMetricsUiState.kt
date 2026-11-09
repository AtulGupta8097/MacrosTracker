package com.macrivo.app.presentation.home.model

import androidx.compose.runtime.Immutable
import com.macrivo.app.domain.model.health.BmiStatus

@Immutable
data class HealthMetricsUiState(
    val bmiText: String = "—",
    val bmiStatus: BmiStatus = BmiStatus.UNKNOWN,
    val tdeeText: String = "—",
    val bmrText: String = "—",
)
