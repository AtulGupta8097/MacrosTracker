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
import com.example.responsiveapp.domain.model.NutritionInfo
import com.example.responsiveapp.domain.model.foodlog.FoodLog
import com.example.responsiveapp.presentation.ui.theme.ResponsiveAppTheme
import com.example.responsiveapp.presentation.ui.theme.spacing

@Composable
fun FoodLogsSection(
    foodLogs: List<FoodLog>,
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

        if (foodLogs.isEmpty()) {
            EmptyFoodLogsState()
        } else {
            foodLogs.forEach { foodLog ->
                FoodLogCard(
                    foodLog = foodLog,
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
            foodLogs = listOf(
                FoodLog(
                    id = "1",
                    foodName = "Grilled chicken breast",
                    nutrition = NutritionInfo(
                        calories = 248f,
                        protein = 46.5f,
                        carbs = 0f,
                        fat = 5.4f,
                    ),
                    createdAt = System.currentTimeMillis(),
                ),
                FoodLog(
                    id = "2",
                    foodName = "Brown rice bowl",
                    nutrition = NutritionInfo(
                        calories = 320f,
                        protein = 8f,
                        carbs = 62f,
                        fat = 4f,
                    ),
                    createdAt = System.currentTimeMillis(),
                ),
            ),
        )
    }
}