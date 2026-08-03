package com.example.responsiveapp.presentation.home.componet.nutrition

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.responsiveapp.presentation.home.model.NutritionUiState
import com.example.responsiveapp.presentation.ui.theme.ResponsiveAppTheme
import com.example.responsiveapp.presentation.ui.theme.spacing

@Composable
fun NutritionProgressSection(
    nutritionUiState: NutritionUiState,
    onLogFoodClick: () -> Unit,
    modifier: Modifier = Modifier,
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
        )

        MacroProgressGrid(
            macros = nutritionUiState.macros,
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