package com.macrivo.app.presentation.home.mapper

import com.macrivo.app.domain.model.health.BmiStatus
import com.macrivo.app.domain.model.macros.MacroTarget
import com.macrivo.app.presentation.home.model.HealthMetricsUiState

fun MacroTarget?.toHealthMetricsUiState(): HealthMetricsUiState {

    if (this == null) {
        return HealthMetricsUiState()
    }

    return HealthMetricsUiState(
        bmiText = "%.1f".format(bmi),
        bmiStatus = BmiStatus.fromBmi(bmi),
        tdeeText = tdee.toString(),
        bmrText = bmr.toString(),
    )
}
