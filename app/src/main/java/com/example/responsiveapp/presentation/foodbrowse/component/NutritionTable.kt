package com.example.responsiveapp.presentation.foodbrowse.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.responsiveapp.domain.model.NutritionInfo
import com.example.responsiveapp.presentation.foodbrowse.fmt
import com.example.responsiveapp.presentation.ui.theme.ResponsiveAppTheme
import com.example.responsiveapp.presentation.ui.theme.spacing
import kotlin.math.roundToInt

@Composable
fun NutritionTable(
    nutrition: NutritionInfo,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        shape = MaterialTheme.shapes.large,
        tonalElevation = 1.dp,
        shadowElevation = 1.dp,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    vertical = MaterialTheme.spacing.xs,
                ),
        ) {

            NutritionRow(
                "Total Fat",
                "${nutrition.fat.fmt()}g",
                isHeader = true,
            )

            NutritionRow(
                "Saturated Fat",
                "${nutrition.saturatedFat.fmt()}g",
                indent = true,
            )

            NutritionRow(
                "Trans Fat",
                "${nutrition.transFat.fmt()}g",
                indent = true,
            )

            NutritionRow(
                "Polyunsaturated",
                "${nutrition.polyunsaturatedFat.fmt()}g",
                indent = true,
            )

            NutritionRow(
                "Monounsaturated",
                "${nutrition.monounsaturatedFat.fmt()}g",
                indent = true,
            )

            TableDivider()

            NutritionRow(
                "Cholesterol",
                "${nutrition.cholesterol.roundToInt()}mg",
                isHeader = true,
            )

            NutritionRow(
                "Sodium",
                "${nutrition.sodium.roundToInt()}mg",
                isHeader = true,
            )

            TableDivider()

            NutritionRow(
                "Total Carbs",
                "${nutrition.carbs.fmt()}g",
                isHeader = true,
            )

            NutritionRow(
                "Dietary Fiber",
                "${nutrition.fiber.fmt()}g",
                indent = true,
            )

            NutritionRow(
                "Total Sugars",
                "${nutrition.sugar.fmt()}g",
                indent = true,
            )

            NutritionRow(
                "Added Sugars",
                "${nutrition.addedSugars.fmt()}g",
                indent = true,
            )

            TableDivider()

            NutritionRow(
                "Protein",
                "${nutrition.protein.fmt()}g",
                isHeader = true,
            )

            TableDivider(thick = true)

            NutritionRow(
                "Potassium",
                "${nutrition.potassium.roundToInt()}mg",
            )

            NutritionRow(
                "Vitamin A",
                "${nutrition.vitaminA.roundToInt()}mcg",
            )

            NutritionRow(
                "Vitamin C",
                "${nutrition.vitaminC.fmt()}mg",
            )

            NutritionRow(
                "Vitamin D",
                "${nutrition.vitaminD.fmt()}mcg",
            )

            NutritionRow(
                "Calcium",
                "${nutrition.calcium.roundToInt()}mg",
            )

            NutritionRow(
                "Iron",
                "${nutrition.iron.fmt()}mg",
            )
        }
    }
}

@Preview
@Composable
private fun PreNutritionTable() {
    ResponsiveAppTheme {
        NutritionTable(
            nutrition = NutritionInfo(
                calories = 250f,
                protein = 10f,
                carbs = 30f,
                fat = 8f,
                fiber = 5f,
                sugar = 12f,
                sodium = 300f,
                cholesterol = 50f,
                saturatedFat = 3f,
                polyunsaturatedFat = 2f,
                monounsaturatedFat = 3f,
                transFat = 0.5f,
                potassium = 400f,
                addedSugars = 5f,
                vitaminA = 500f,
                vitaminC = 20f,
                vitaminD = 2.5f,
                calcium = 150f,
                iron = 2.5f,
            )
        )
    }
}