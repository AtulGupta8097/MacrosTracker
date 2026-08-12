package com.example.responsiveapp.presentation.progress.mapper

import com.example.responsiveapp.core.utils.DateUtils
import com.example.responsiveapp.domain.model.DailySummary
import com.example.responsiveapp.presentation.progress.model.MacroChartPointUi
import com.example.responsiveapp.presentation.progress.model.MacroType
import com.example.responsiveapp.presentation.progress.model.NutritionAverageUiState
import com.example.responsiveapp.presentation.progress.model.NutritionOverviewUiState
import kotlin.math.roundToInt

fun List<DailySummary>.toNutritionOverviewUiState(
    selectedMacro: MacroType,
): NutritionOverviewUiState {

    val average = NutritionAverageUiState(
        calories = averageOf { it.consumed.calories },
        protein = averageOf { it.consumed.protein },
        carbs = averageOf { it.consumed.carbs },
        fats = averageOf { it.consumed.fats },
    )

    val chartData = map { summary ->
        summary.toMacroChartPointUi(selectedMacro)
    }

    return NutritionOverviewUiState(
        selectedMacro = selectedMacro,
        average = average,
        chartData = chartData,
    )
}

private fun List<DailySummary>.averageOf(
    selector: (DailySummary) -> Float,
): Int =
    if (isEmpty()) {
        0
    } else {
        (sumOf { selector(it).toDouble() } / size)
            .roundToInt()
    }

private fun DailySummary.toMacroChartPointUi(
    macroType: MacroType,
): MacroChartPointUi {

    val (consumedValue, targetValue) = when (macroType) {

        MacroType.PROTEIN ->
            consumed.protein to target.protein.toFloat()

        MacroType.CARBS ->
            consumed.carbs to target.carbs.toFloat()

        MacroType.FATS ->
            consumed.fats to target.fats.toFloat()
    }

    return MacroChartPointUi(
        label = DateUtils.formatShortDate(date),
        consumed = consumedValue,
        target = targetValue,
    )
}