package com.macrivo.app.domain.model.macros

import com.macrivo.app.domain.model.NutritionTargets

data class CalculationResult(
    val targets: NutritionTargets,
    val bmr: Int,
    val tdee: Int,
    val bmi: Float
)