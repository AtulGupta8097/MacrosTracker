package com.example.responsiveapp.presentation.mymeal

import com.example.responsiveapp.domain.model.MealIngredient

data class TotalMacros(
    val calories: Float = 0f,
    val protein: Float = 0f,
    val carbs: Float = 0f,
    val fat: Float = 0f,
)

fun Map<String, MealIngredient>.totalMacros(): TotalMacros {

    return values.fold(TotalMacros()) { total, ingredient ->

        val nutrition = ingredient.nutrition

        total.copy(
            calories = total.calories + nutrition.calories,
            protein = total.protein + nutrition.protein,
            carbs = total.carbs + nutrition.carbs,
            fat = total.fat + nutrition.fat
        )
    }
}