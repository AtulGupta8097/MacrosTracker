package com.example.responsiveapp.presentation.home.mapper

import com.example.responsiveapp.domain.model.health.BmiStatus
import com.example.responsiveapp.domain.model.macros.MacroTarget
import com.example.responsiveapp.presentation.home.model.HealthMetricsUiState

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
