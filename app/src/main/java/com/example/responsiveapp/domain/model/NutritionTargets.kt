package com.example.responsiveapp.domain.model

data class NutritionTargets(
    val calories: Int = 0,
    val protein: Int = 0,
    val carbs: Int = 0,
    val fats: Int = 0,
    val fiber: Int = 0,
    val sugarLimit: Int = 0,
    val sodiumLimit: Int = 0
)