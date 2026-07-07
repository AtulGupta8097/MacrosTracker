package com.example.responsiveapp.domain.model

import androidx.compose.runtime.Immutable

@Immutable
data class NutritionProgress(
    val calories: Float = 0f,
    val protein: Float = 0f,
    val carbs: Float = 0f,
    val fats: Float = 0f,
    val fiber: Float = 0f,
    val sugar: Float = 0f,
    val sodium: Float = 0f
)

operator fun NutritionProgress.plus(
    info: NutritionInfo
): NutritionProgress {
    return NutritionProgress(
        calories = calories + info.calories,
        protein = protein + info.protein,
        carbs = carbs + info.carbs,
        fats = fats + info.fat,
        fiber = fiber + info.fiber,
        sugar = sugar + info.sugar,
        sodium = sodium + info.sodium
    )
}