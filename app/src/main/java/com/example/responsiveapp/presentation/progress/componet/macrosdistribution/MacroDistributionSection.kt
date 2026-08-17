package com.example.responsiveapp.presentation.progress.componet.macrosdistribution

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.responsiveapp.presentation.progress.model.MacroDistributionUiState
import com.example.responsiveapp.presentation.progress.model.MacroShareUiState
import com.example.responsiveapp.presentation.ui.theme.spacing

@Composable
fun MacroDistributionSection(
    state: MacroDistributionUiState,
    modifier: Modifier = Modifier,
) {
    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(20.dp),
            )
            .padding(MaterialTheme.spacing.md),
    ) {
        val isCompact = maxWidth < 300.dp

        Column {
            MacroDistributionHeader()

            if (isCompact) {
                // Very narrow screens
                MacroDonutChart(
                    state = state,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = MaterialTheme.spacing.md)
                        .size(170.dp),
                )

                MacroLegend(
                    proteinGram = state.protein.grams,
                    carbsGram = state.carbs.grams,
                    fatGram = state.fats.grams,
                    compact = true,
                    modifier = Modifier.padding(top = MaterialTheme.spacing.md),
                )
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = MaterialTheme.spacing.md),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.md),
                ) {
                    MacroDonutChart(
                        state = state,
                        modifier = Modifier
                            .weight(1.2f)
                            .aspectRatio(1f),
                    )

                    MacroLegend(
                        proteinGram = state.protein.grams,
                        carbsGram = state.carbs.grams,
                        fatGram = state.fats.grams,
                        compact = false,
                        modifier = Modifier.weight(0.8f),
                    )
                }
            }

            MacroStats(
                state = state,
                modifier = Modifier.padding(top = MaterialTheme.spacing.md),
            )
        }
    }
}

@Preview
@Composable
private fun MacroDistributionSectionPreview() {
    MacroDistributionSection(
        state = MacroDistributionUiState(
            protein = MacroShareUiState(grams = 100, percentOfCalories = 10, calorieShare = 0.1f),
            carbs = MacroShareUiState(grams = 100, percentOfCalories = 80, calorieShare = 0.8f),
            fats = MacroShareUiState(grams = 100, percentOfCalories = 10, calorieShare = 0.1f),
        ),
    )
}