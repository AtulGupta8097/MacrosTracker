package com.example.responsiveapp.presentation.progress.mapper

import com.example.responsiveapp.presentation.progress.model.MacroDistributionUiState
import com.example.responsiveapp.presentation.progress.model.MacroShareUiState
import com.example.responsiveapp.presentation.progress.model.NutritionAverageUiState
import kotlin.math.roundToInt

private const val PROTEIN_KCAL_PER_GRAM = 4
private const val CARBS_KCAL_PER_GRAM = 4
private const val FAT_KCAL_PER_GRAM = 9

fun NutritionAverageUiState.toMacroDistributionUiState(): MacroDistributionUiState {
    val proteinCalories = protein * PROTEIN_KCAL_PER_GRAM
    val carbsCalories = carbs * CARBS_KCAL_PER_GRAM
    val fatCalories = fats * FAT_KCAL_PER_GRAM
    val totalCalories = proteinCalories + carbsCalories + fatCalories

    return MacroDistributionUiState(
        hasData = totalCalories > 0,
        protein = macroShare(grams = protein, calories = proteinCalories, totalCalories = totalCalories),
        carbs = macroShare(grams = carbs, calories = carbsCalories, totalCalories = totalCalories),
        fats = macroShare(grams = fats, calories = fatCalories, totalCalories = totalCalories),
    )
}

private fun macroShare(grams: Int, calories: Int, totalCalories: Int): MacroShareUiState {
    if (totalCalories <= 0) return MacroShareUiState(grams = grams)
    val share = calories.toFloat() / totalCalories
    return MacroShareUiState(
        grams = grams,
        percentOfCalories = (share * 100).roundToInt(),
        calorieShare = share,
    )
}