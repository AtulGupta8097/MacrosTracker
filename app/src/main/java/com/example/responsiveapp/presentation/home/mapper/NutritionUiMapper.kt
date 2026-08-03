package com.example.responsiveapp.presentation.home.mapper

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Grain
import androidx.compose.material.icons.filled.Opacity
import com.example.responsiveapp.core.utils.formatMacroValue
import com.example.responsiveapp.domain.model.DailySummary
import com.example.responsiveapp.domain.model.NutritionProgress
import com.example.responsiveapp.domain.model.NutritionTargets
import com.example.responsiveapp.presentation.home.model.CaloriesUiModel
import com.example.responsiveapp.presentation.home.model.MacroProgressUiModel
import com.example.responsiveapp.presentation.home.model.NutritionUiState
import com.example.responsiveapp.presentation.ui.theme.CarbsColor
import com.example.responsiveapp.presentation.ui.theme.FatColor
import com.example.responsiveapp.presentation.ui.theme.FiberColor
import com.example.responsiveapp.presentation.ui.theme.ProteinColor
import kotlin.math.roundToInt

fun DailySummary?.toNutritionUiState(): NutritionUiState {

    val target = this?.target ?: NutritionTargets()
    val consumed = this?.consumed ?: NutritionProgress()

    val calories = CaloriesUiModel(
        consumedText = consumed.calories.roundToInt().toString(),
        targetText = " / ${target.calories} kcal",
        remainingText = remainingText(
            consumed = consumed.calories,
            target = target.calories,
            unit = "kcal",
        ),
        progress = progressOf(
            consumed = consumed.calories,
            target = target.calories,
        ),
    )

    val macros = listOf(
        macroProgressOf(
            key = "protein",
            icon = Icons.Default.FitnessCenter,
            label = "Protein",
            consumedGrams = consumed.protein,
            targetGrams = target.protein,
            accentColor = ProteinColor,
        ),
        macroProgressOf(
            key = "carbs",
            icon = Icons.Default.Grain,
            label = "Carbs",
            consumedGrams = consumed.carbs,
            targetGrams = target.carbs,
            accentColor = CarbsColor,
        ),
        macroProgressOf(
            key = "fats",
            icon = Icons.Default.Opacity,
            label = "Fats",
            consumedGrams = consumed.fats,
            targetGrams = target.fats,
            accentColor = FatColor,
        ),
        macroProgressOf(
            key = "fiber",
            icon = Icons.Default.Eco,
            label = "Fiber",
            consumedGrams = consumed.fiber,
            targetGrams = target.fiber,
            accentColor = FiberColor,
        ),
    )

    return NutritionUiState(
        calories = calories,
        macros = macros,
    )
}

private fun macroProgressOf(
    key: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    consumedGrams: Float,
    targetGrams: Int,
    accentColor: androidx.compose.ui.graphics.Color,
): MacroProgressUiModel =
    MacroProgressUiModel(
        key = key,
        icon = icon,
        label = label,
        consumedText = formatMacroValue(consumedGrams),
        targetText = " / $targetGrams g",
        remainingText = remainingText(
            consumed = consumedGrams,
            target = targetGrams,
            unit = "g",
        ),
        progress = progressOf(
            consumed = consumedGrams,
            target = targetGrams,
        ),
        accentColor = accentColor,
    )

private fun progressOf(consumed: Float, target: Int): Float =
    if (target > 0) (consumed / target).coerceIn(0f, 1f) else 0f

private fun remainingText(consumed: Float, target: Int, unit: String): String {
    val remaining = (target - consumed).roundToInt()
    return if (remaining >= 0) {
        "$remaining $unit left"
    } else {
        "${-remaining} $unit over"
    }
}
