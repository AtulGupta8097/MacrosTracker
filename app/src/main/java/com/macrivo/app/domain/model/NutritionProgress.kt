package com.macrivo.app.domain.model

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

operator fun NutritionProgress.minus(
    info: NutritionInfo
): NutritionProgress {
    return NutritionProgress(
        calories = (calories - info.calories).coerceAtLeast(0f),
        protein = (protein - info.protein).coerceAtLeast(0f),
        carbs = (carbs - info.carbs).coerceAtLeast(0f),
        fats = (fats - info.fat).coerceAtLeast(0f),
        fiber = (fiber - info.fiber).coerceAtLeast(0f),
        sugar = (sugar - info.sugar).coerceAtLeast(0f),
        sodium = (sodium - info.sodium).coerceAtLeast(0f)
    )
}
