package com.example.responsiveapp.domain.model.macros

import com.example.responsiveapp.domain.model.NutritionTargets

data class CalculationResult(
    val targets: NutritionTargets,
    val bmr: Int,
    val tdee: Int
)