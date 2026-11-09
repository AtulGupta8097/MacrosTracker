package com.macrivo.app.presentation.home.mapper

import com.macrivo.app.core.utils.DateUtils
import com.macrivo.app.core.utils.formatMacroValue
import com.macrivo.app.domain.model.foodlog.FoodLog
import com.macrivo.app.presentation.home.model.FoodLogUiModel
import com.macrivo.app.presentation.home.model.RecentMealsUiState

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
