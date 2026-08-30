package com.example.responsiveapp.presentation.progress.componet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Grain
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Opacity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.responsiveapp.presentation.progress.componet.chart.MacroSelector
import com.example.responsiveapp.presentation.progress.componet.chart.NutritionTrendChart
import com.example.responsiveapp.presentation.progress.model.MacroType
import com.example.responsiveapp.presentation.progress.model.NutritionOverviewUiState
import com.example.responsiveapp.presentation.progress.model.ProgressRange
import com.example.responsiveapp.presentation.ui.theme.CaloriesColor
import com.example.responsiveapp.presentation.ui.theme.CarbsColor
import com.example.responsiveapp.presentation.ui.theme.FatColor
import com.example.responsiveapp.presentation.ui.theme.ProteinColor
import com.example.responsiveapp.presentation.ui.theme.spacing

@Composable
fun NutritionOverviewSection(
    state: NutritionOverviewUiState,
    range: ProgressRange,
    selectedMacro: MacroType,
    onMacroSelected: (MacroType) -> Unit,
    modifier: Modifier = Modifier,
) {
    ProgressCard(modifier = modifier, title = "Nutrition Overview") {

        Row(horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.sm)) {
            StatTile(
                icon = Icons.Default.LocalFireDepartment,
                label = "Avg Calories",
                value = state.average.calories.toString(),
                unit = "kcal",
                progress = 1f,
                accentColor = MaterialTheme.colorScheme.primary,
                modifier = Modifier.weight(1f),
            )
            StatTile(
                icon = Icons.Default.FitnessCenter,
                label = "Avg Protein",
                value = state.average.protein.toString(),
                unit = "g",
                progress = 1f,
                accentColor = ProteinColor,
                modifier = Modifier.weight(1f),
            )
        }

        Row(
            modifier = Modifier.padding(top = MaterialTheme.spacing.sm),
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.sm),
        ) {
            StatTile(
                icon = Icons.Default.Grain,
                label = "Avg Carbs",
                value = state.average.carbs.toString(),
                unit = "g",
                progress = 1f,
                accentColor = CarbsColor,
                modifier = Modifier.weight(1f),
            )
            StatTile(
                icon = Icons.Default.Opacity,
                label = "Avg Fat",
                value = state.average.fats.toString(),
                unit = "g",
                progress = 1f,
                accentColor = FatColor,
                modifier = Modifier.weight(1f),
            )
        }

        MacroSelector(
            selected = selectedMacro,
            onMacroSelected = onMacroSelected,
            modifier = Modifier.padding(top = MaterialTheme.spacing.md, bottom = MaterialTheme.spacing.sm),
        )

        key(selectedMacro, range) {
            NutritionTrendChart(
                points = state.chartData,
                range = range,
                macroColor = selectedMacro.chartColor(),
                unit = if (selectedMacro == MacroType.CALORIES) "kcal" else "g",
                hasData = state.hasData,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

private fun MacroType.chartColor(): Color = when (this) {
    MacroType.CALORIES -> CaloriesColor
    MacroType.PROTEIN -> ProteinColor
    MacroType.CARBS -> CarbsColor
    MacroType.FATS -> FatColor
}


@Composable
fun ProgressCard(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(20.dp))
            .padding(MaterialTheme.spacing.md)
    ) {
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = MaterialTheme.spacing.md),
            )
            content()
        }
    }
}