package com.macrivo.app.presentation.home.componet.nutrition

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.macrivo.app.presentation.home.model.MacroTargetField
import com.macrivo.app.presentation.home.model.NutritionUiState
import com.macrivo.app.presentation.ui.theme.ResponsiveAppTheme
import com.macrivo.app.presentation.ui.theme.spacing

@Composable
fun NutritionProgressSection(
    nutritionUiState: NutritionUiState,
    onLogFoodClick: () -> Unit,
    modifier: Modifier = Modifier,
    onEditTarget: (String) -> Unit = {},
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = MaterialTheme.spacing.md,
            ),
        verticalArrangement = Arrangement.spacedBy(
            MaterialTheme.spacing.md,
        ),
    ) {

        CaloriesCard(
            calories = nutritionUiState.calories,
            onLogFoodClick = onLogFoodClick,
            onEditTarget = if (nutritionUiState.canEdit) {
                { onEditTarget(MacroTargetField.Calories.key) }
            } else null,
        )

        MacroProgressGrid(
            macros = nutritionUiState.macros,
            onEditTarget = if (nutritionUiState.canEdit) onEditTarget else null,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun NutritionProgressSectionPreview() {
    ResponsiveAppTheme {
        NutritionProgressSection(
            nutritionUiState = NutritionUiState(),
            onLogFoodClick = {},
        )
    }
}