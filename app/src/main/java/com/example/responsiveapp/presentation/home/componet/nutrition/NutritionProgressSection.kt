package com.example.responsiveapp.presentation.home.componet.nutrition

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.responsiveapp.domain.model.DailySummary
import com.example.responsiveapp.domain.model.NutritionProgress
import com.example.responsiveapp.domain.model.NutritionTargets
import com.example.responsiveapp.presentation.ui.theme.ResponsiveAppTheme
import com.example.responsiveapp.presentation.ui.theme.spacing

@Composable
fun NutritionProgressSection(
    dailySummary: DailySummary?,
    onLogFoodClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val target =
        dailySummary?.target ?: NutritionTargets()

    val consumed =
        dailySummary?.consumed ?: NutritionProgress()

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

//        CaloriesCard(
//            consumedCalories = consumed.calories,
//            targetCalories = target.calories,
//            onLogFoodClick = onLogFoodClick,
//        )
//
//        MacroProgressGrid(
//            target = target,
//            consumed = consumed,
//        )
    }
}

@Preview(showBackground = true)
@Composable
private fun NutritionProgressSectionPreview() {
    ResponsiveAppTheme {
        NutritionProgressSection(
            dailySummary = DailySummary(
                date = 0L,
                target = NutritionTargets(
                    calories = 1_863,
                    protein = 140,
                    carbs = 220,
                    fats = 70,
                    fiber = 30,
                ),
                consumed = NutritionProgress(
                    calories = 1_340f,
                    protein = 98f,
                    carbs = 145f,
                    fats = 46f,
                    fiber = 18f,
                ),
                createdAt = 0L,
                updatedAt = 0L,
            ),
            onLogFoodClick = {},
        )
    }
}