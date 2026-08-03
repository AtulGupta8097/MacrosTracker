package com.example.responsiveapp.presentation.home.mapper

import com.example.responsiveapp.core.utils.DateUtils
import com.example.responsiveapp.core.utils.formatMacroValue
import com.example.responsiveapp.domain.model.foodlog.FoodLog
import com.example.responsiveapp.presentation.home.model.FoodLogUiModel
import com.example.responsiveapp.presentation.home.model.RecentMealsUiState

fun FoodLog.toUiModel(): FoodLogUiModel =
    FoodLogUiModel(
        id = id,
        name = foodName,
        timeText = DateUtils.formatTimeOfDay(createdAt),
        caloriesText = "${nutrition.calories.toInt()} kcal",
        proteinText = "${formatMacroValue(nutrition.protein)}g",
        carbsText = "${formatMacroValue(nutrition.carbs)}g",
        fatText = "${formatMacroValue(nutrition.fat)}g",
    )

fun List<FoodLog>.toRecentMealsUiState(): RecentMealsUiState =
    RecentMealsUiState(
        meals = map { it.toUiModel() }
    )
