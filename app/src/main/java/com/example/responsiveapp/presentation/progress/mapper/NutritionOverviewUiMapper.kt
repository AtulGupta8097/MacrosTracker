package com.example.responsiveapp.presentation.progress.mapper

import com.example.responsiveapp.core.utils.DateUtils
import com.example.responsiveapp.domain.model.DailySummary
import com.example.responsiveapp.presentation.progress.model.MacroChartPointUi
import com.example.responsiveapp.presentation.progress.model.MacroType
import com.example.responsiveapp.presentation.progress.model.NutritionAverageUiState
import com.example.responsiveapp.presentation.progress.model.NutritionOverviewUiState
import com.example.responsiveapp.presentation.progress.model.ProgressRange
import kotlin.math.roundToInt

fun List<DailySummary>.toNutritionOverviewUiState(
    range: ProgressRange,
    selectedMacro: MacroType,
): NutritionOverviewUiState {
    val today = DateUtils.today()
    val startDate = DateUtils.minusDays(today, (range.days - 1).toLong())

    val summariesByDate = filter { it.date in startDate..today }
        .associateBy { it.date }

    val allDatesInRange = (0 until range.days).map { offset ->
        DateUtils.minusDays(today, (range.days - 1 - offset).toLong())
    }

    val loggedSummaries = allDatesInRange.mapNotNull { summariesByDate[it] }

    val average = NutritionAverageUiState(
        calories = loggedSummaries.averageOf { it.consumed.calories },
        protein = loggedSummaries.averageOf { it.consumed.protein },
        carbs = loggedSummaries.averageOf { it.consumed.carbs },
        fats = loggedSummaries.averageOf { it.consumed.fats },
    )

    val chartData = allDatesInRange.map { date ->
        val summary = summariesByDate[date]
        summary.toMacroChartPointUi(date, selectedMacro)
    }

    return NutritionOverviewUiState(
        hasData = loggedSummaries.isNotEmpty(),
        average = average,
        chartData = chartData,
    )
}

private fun List<DailySummary>.averageOf(selector: (DailySummary) -> Float): Int =
    if (isEmpty()) 0 else (sumOf { selector(it).toDouble() } / size).roundToInt()

private fun DailySummary?.toMacroChartPointUi(date: Long, macroType: MacroType): MacroChartPointUi {
    if (this == null) {
        return MacroChartPointUi(
            label = DateUtils.formatShortDate(date),
            consumed = 0f,
            target = 0f,
        )
    }
    val (consumedValue, targetValue) = when (macroType) {
        MacroType.CALORIES -> consumed.calories to target.calories.toFloat()
        MacroType.PROTEIN -> consumed.protein to target.protein.toFloat()
        MacroType.CARBS -> consumed.carbs to target.carbs.toFloat()
        MacroType.FATS -> consumed.fats to target.fats.toFloat()
    }
    return MacroChartPointUi(
        label = DateUtils.formatShortDate(date),
        consumed = consumedValue,
        target = targetValue,
    )
}