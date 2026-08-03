package com.example.responsiveapp.presentation.home.componet.nutrition

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.responsiveapp.presentation.home.model.CaloriesUiModel
import com.example.responsiveapp.presentation.ui.theme.ResponsiveAppTheme
import com.example.responsiveapp.presentation.ui.theme.spacing

@Composable
fun CaloriesCard(
    modifier: Modifier = Modifier,
    calories: CaloriesUiModel,
    onLogFoodClick: () -> Unit,
) {

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp,
        ),
    ) {
        Column(
            modifier = Modifier.padding(
                MaterialTheme.spacing.md,
            ),
            verticalArrangement = Arrangement.spacedBy(
                MaterialTheme.spacing.sm,
            ),
        ) {

            CaloriesHeader()

            BoxWithConstraints {

                val isWideLayout =
                    maxWidth >= 260.dp

                if (isWideLayout) {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(
                            MaterialTheme.spacing.md,
                        ),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {

                        CaloriesDetails(
                            calories = calories,
                            onLogFoodClick = onLogFoodClick,
                            modifier = Modifier.weight(1f),
                        )
                        Box(
                            modifier = Modifier.weight(0.6f),
                        ) {
                            CaloriesProgressRing(
                                progress = calories.progress,
                            )

                        }
                    }

                } else {

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(
                            MaterialTheme.spacing.md,
                        ),
                    ) {

                        CaloriesProgressRing(
                            progress = calories.progress,
                        )

                        CaloriesDetails(
                            calories = calories,
                            onLogFoodClick = onLogFoodClick,
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewCaloriesCard() {
    ResponsiveAppTheme {
        CaloriesCard(
            calories = CaloriesUiModel(
                consumedText = "1340",
                targetText = " / 1863 kcal",
                remainingText = "523 kcal left",
                progress = 0.72f,
            ),
            onLogFoodClick = {},
        )
    }
}