package com.macrivo.app.presentation.home.mapper

import com.macrivo.app.core.utils.DateUtils
import com.macrivo.app.core.utils.formatMacroValue
import com.macrivo.app.domain.model.Serving
import com.macrivo.app.domain.model.ServingUnit
import com.macrivo.app.domain.model.scale
import com.macrivo.app.domain.model.foodlog.FoodLog
import com.macrivo.app.domain.model.foodlog.isEditable
import com.macrivo.app.presentation.foodbrowse.FoodDetailMode
import com.macrivo.app.presentation.foodbrowse.FoodDetailUiState
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
        canDelete = isEditable,
    )

fun List<FoodLog>.toRecentMealsUiState(): RecentMealsUiState =
    RecentMealsUiState(
        meals = map { it.toUiModel() }
    )


fun FoodLog.toFoodDetailUiState(
    isBusy: Boolean = false,
    editedQuantity: Float = quantity,
): FoodDetailUiState.Success {
    val serving = servingDescription.ifBlank { "serving" }
    val shownQuantity = editedQuantity
    val shownNutrition =
        if (quantity > 0f && shownQuantity != quantity) nutrition.scale(shownQuantity / quantity) else nutrition

    val loggedServing = Serving(
        id = "logged",
        description = serving,
        metricAmount = 0f,
        metricUnit = ServingUnit.GRAM,
        numberOfUnits = 1f,
        measurementDescription = "serving",
        nutrition = shownNutrition,
        isDefault = true,
    )

    return FoodDetailUiState.Success(
        foodName = foodName,
        foodBrand = "${DateUtils.formatFullDate(date)} \u00b7 ${DateUtils.formatTimeOfDay(createdAt)}",
        servings = listOf(loggedServing),
        selectedServing = loggedServing,
        quantity = shownQuantity,
        scaledNutrition = shownNutrition,
        nutritionLabel = if (shownQuantity == 1f) "per $serving" else "per ${formatMacroValue(shownQuantity)} \u00d7 $serving",
        mode = if (isEditable) FoodDetailMode.LOGGED_TODAY else FoodDetailMode.LOGGED_PAST,
        isBusy = isBusy,
        hasChanges = shownQuantity != quantity,
    )
}
