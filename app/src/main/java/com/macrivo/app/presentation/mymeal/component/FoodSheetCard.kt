package com.macrivo.app.presentation.mymeal.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.macrivo.app.core.utils.formatMacroValue
import com.macrivo.app.domain.model.FoodItem
import com.macrivo.app.domain.model.MacroSummary
import com.macrivo.app.presentation.commoncomponent.CircularIconButton
import com.macrivo.app.presentation.commoncomponent.FoodLogCard
import com.macrivo.app.presentation.ui.theme.ResponsiveAppTheme

@Composable
fun FoodSheetCard(
    food: FoodItem,
    onAdd: () -> Unit,
) {
    FoodLogCard(
        name = food.name,
        subtitle = food.brand?.takeIf { it.isNotBlank() },
        caloriesText = "${food.macroSummary.calories.toInt()} kcal",
        proteinText = formatMacroValue(food.macroSummary.protein),
        carbsText = formatMacroValue(food.macroSummary.carbs),
        fatText = formatMacroValue(food.macroSummary.fat),
        onClick = onAdd,
        trailingContent = {
            CircularIconButton(
                icon = Icons.Default.Add,
                contentDescription = "Add food",
                onClick = onAdd,
            )
        },
    )
}

@Preview
@Composable
private fun PrevFoodSheetCard() {
    ResponsiveAppTheme {
        FoodSheetCard(
            food = FoodItem(
                id = "1334",
                name = "Pizza",
                brand = "Pizza Hut",
                macroSummary = MacroSummary(
                    servingLabel = "1 slice",
                    calories = 265f,
                    protein = 26f,
                    fat = 2f,
                    carbs = 7f
                )
            ),
            onAdd = {}
        )
    }
}