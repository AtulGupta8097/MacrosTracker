package com.example.responsiveapp.presentation.home.componet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.example.responsiveapp.presentation.home.model.FoodLogUiModel
import com.example.responsiveapp.presentation.home.model.RecentMealsUiState
import com.example.responsiveapp.presentation.ui.theme.ResponsiveAppTheme
import com.example.responsiveapp.presentation.ui.theme.spacing

@Composable
fun FoodLogsSection(
    recentMealsUiState: RecentMealsUiState,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = MaterialTheme.spacing.md),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.sm),
    ) {

        Text(
            text = "Recently Logged",
            style = MaterialTheme.typography.titleMedium
                .copy(
                    fontWeight = FontWeight.Bold,
                ),
            color = MaterialTheme.colorScheme.onSurface,
        )

        if (recentMealsUiState.isEmpty) {
            EmptyFoodLogsState()
        } else {
            recentMealsUiState.meals.forEach { meal ->
                FoodLogCard(
                    meal = meal,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PrevFoodLogsSection() {
    ResponsiveAppTheme {
        FoodLogsSection(
            recentMealsUiState = RecentMealsUiState(
                meals = listOf(
                    FoodLogUiModel(
                        id = "1",
                        name = "Grilled chicken breast",
                        timeText = "1:15 PM",
                        caloriesText = "248 kcal",
                        proteinText = "46.5g",
                        carbsText = "0g",
                        fatText = "5.4g",
                    ),
                    FoodLogUiModel(
                        id = "2",
                        name = "Brown rice bowl",
                        timeText = "2:05 PM",
                        caloriesText = "320 kcal",
                        proteinText = "8g",
                        carbsText = "62g",
                        fatText = "4g",
                    ),
                ),
            ),
        )
    }
}