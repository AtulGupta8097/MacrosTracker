package com.macrivo.app.presentation.myfood.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.WarningAmber
import androidx.compose.material3.Icon
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.macrivo.app.presentation.myfood.MacronutrientsState
import com.macrivo.app.presentation.myfood.MineralsState
import com.macrivo.app.presentation.myfood.VitaminsState
import com.macrivo.app.presentation.myfood.validation.NutrientErrors
import com.macrivo.app.presentation.myfood.validation.NutrientField
import com.macrivo.app.presentation.ui.theme.CaloriesColor
import com.macrivo.app.presentation.ui.theme.CarbsColor
import com.macrivo.app.presentation.ui.theme.FatColor
import com.macrivo.app.presentation.ui.theme.ProteinColor
import com.macrivo.app.presentation.ui.theme.ResponsiveAppTheme
import com.macrivo.app.presentation.ui.theme.spacing

private val SodiumColor = Color(0xFFFFC107)
private val CholesterolColor = Color(0xFFFF5252)
private val PotassiumColor = Color(0xFF5C6BC0)
private val CalciumColor = Color(0xFF00BCD4)
private val IronColor = Color(0xFF9E9E9E)
private val VitaminAColor = Color(0xFFFF5252)
private val VitaminCColor = Color(0xFFFF9800)
private val VitaminDColor = Color(0xFFFFEB3B)
private val FiberColor = Color(0xFF8B4513)
private val SugarColor = Color(0xFFE91E8C)

@Composable
fun NutrientsStep(
    foodName: String,
    servingText: String,
    macros: MacronutrientsState,
    minerals: MineralsState,
    vitamins: VitaminsState,
    errors: NutrientErrors,
    calorieWarning: String?,
    onCaloriesChanged: (String) -> Unit,
    onProteinChanged: (String) -> Unit,
    onCarbohydratesChanged: (String) -> Unit,
    onTotalFatChanged: (String) -> Unit,
    onFiberChanged: (String) -> Unit,
    onSugarChanged: (String) -> Unit,
    onSodiumChanged: (String) -> Unit,
    onCholesterolChanged: (String) -> Unit,
    onPotassiumChanged: (String) -> Unit,
    onCalciumChanged: (String) -> Unit,
    onIronChanged: (String) -> Unit,
    onVitaminAChanged: (String) -> Unit,
    onVitaminCChanged: (String) -> Unit,
    onVitaminDChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    // CHANGED: plain scrollable Column instead of LazyColumn so every field is always
    // composed - IME "Next" can reach all of them and scroll-to-focus works reliably.
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(MaterialTheme.spacing.md),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.md),
    ) {
        if (foodName.isNotBlank()) {
            Column {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = MaterialTheme.spacing.xs),
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                ) {
                    Text(
                        text = foodName,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                        ),
                        color = MaterialTheme.colorScheme.onBackground,
                    )

                    Text(
                        text = "Serving: $servingText",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }

        Column {
            NutrientGroup(
                title = "Macronutrients",
                accent = CaloriesColor,
            ) {
                NutrientInputRow(
                    label = "Calories",
                    unit = "kcal",
                    value = macros.calories,
                    color = CaloriesColor,
                    onChange = onCaloriesChanged,
                    error = errors[NutrientField.CALORIES],
                )

                NutrientInputRow(
                    label = "Protein",
                    unit = "g",
                    value = macros.protein,
                    color = ProteinColor,
                    onChange = onProteinChanged,
                    error = errors[NutrientField.PROTEIN],
                )

                NutrientInputRow(
                    label = "Carbohydrates",
                    unit = "g",
                    value = macros.carbohydrates,
                    color = CarbsColor,
                    onChange = onCarbohydratesChanged,
                    error = errors[NutrientField.CARBOHYDRATES],
                )

                NutrientInputRow(
                    label = "Total Fat",
                    unit = "g",
                    value = macros.totalFat,
                    color = FatColor,
                    onChange = onTotalFatChanged,
                    error = errors[NutrientField.TOTAL_FAT],
                )

                NutrientInputRow(
                    label = "Fiber",
                    unit = "g",
                    value = macros.fiber,
                    color = FiberColor,
                    onChange = onFiberChanged,
                    error = errors[NutrientField.FIBER],
                )

                NutrientInputRow(
                    label = "Sugar",
                    unit = "g",
                    value = macros.sugar,
                    color = SugarColor,
                    onChange = onSugarChanged,
                    error = errors[NutrientField.SUGAR],
                )
            }
        }

        if (calorieWarning != null) {
            CalorieWarning(message = calorieWarning)
        }

        Column {
            NutrientGroup(
                title = "Minerals",
                accent = SodiumColor,
            ) {
                NutrientInputRow(
                    label = "Sodium",
                    unit = "mg",
                    value = minerals.sodium,
                    color = SodiumColor,
                    onChange = onSodiumChanged,
                    error = errors[NutrientField.SODIUM],
                )

                NutrientInputRow(
                    label = "Cholesterol",
                    unit = "mg",
                    value = minerals.cholesterol,
                    color = CholesterolColor,
                    onChange = onCholesterolChanged,
                    error = errors[NutrientField.CHOLESTEROL],
                )

                NutrientInputRow(
                    label = "Potassium",
                    unit = "mg",
                    value = minerals.potassium,
                    color = PotassiumColor,
                    onChange = onPotassiumChanged,
                    error = errors[NutrientField.POTASSIUM],
                )

                NutrientInputRow(
                    label = "Calcium",
                    unit = "mg",
                    value = minerals.calcium,
                    color = CalciumColor,
                    onChange = onCalciumChanged,
                    error = errors[NutrientField.CALCIUM],
                )

                NutrientInputRow(
                    label = "Iron",
                    unit = "mg",
                    value = minerals.iron,
                    color = IronColor,
                    onChange = onIronChanged,
                    error = errors[NutrientField.IRON],
                )
            }
        }

        Column {
            NutrientGroup(
                title = "Vitamins",
                accent = VitaminCColor,
            ) {
                NutrientInputRow(
                    label = "Vitamin A",
                    unit = "mcg",
                    value = vitamins.vitaminA,
                    color = VitaminAColor,
                    onChange = onVitaminAChanged,
                    error = errors[NutrientField.VITAMIN_A],
                )

                NutrientInputRow(
                    label = "Vitamin C",
                    unit = "mg",
                    value = vitamins.vitaminC,
                    color = VitaminCColor,
                    onChange = onVitaminCChanged,
                    error = errors[NutrientField.VITAMIN_C],
                )

                NutrientInputRow(
                    label = "Vitamin D",
                    unit = "mcg",
                    value = vitamins.vitaminD,
                    color = VitaminDColor,
                    onChange = onVitaminDChanged,
                    error = errors[NutrientField.VITAMIN_D],
                    imeAction = ImeAction.Done,
                )
            }
        }

    }
}

// Soft hint only - never blocks saving and never edits what the user typed.
@Composable
private fun CalorieWarning(message: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(SodiumColor.copy(alpha = 0.14f), MaterialTheme.shapes.medium)
            .padding(MaterialTheme.spacing.sm),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.sm),
    ) {
        Icon(
            imageVector = Icons.Outlined.WarningAmber,
            contentDescription = null,
            tint = SodiumColor,
            modifier = Modifier.size(20.dp),
        )
        Text(
            text = message,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

@Preview
@Composable
private fun PrevNutrientsStep() {
    ResponsiveAppTheme {
        NutrientsStep(
            foodName = "Grilled Chicken Breast",
            servingText = "100 g",
            macros = MacronutrientsState(
                calories = "165",
                protein = "31",
                carbohydrates = "0",
                totalFat = "3.6",
                fiber = "0",
                sugar = "0",
            ),
            minerals = MineralsState(
                sodium = "74",
                cholesterol = "85",
                potassium = "256",
                calcium = "15",
                iron = "1.2",
            ),
            vitamins = VitaminsState(
                vitaminA = "13",
                vitaminC = "0",
                vitaminD = "0.1",
            ),
            errors = NutrientErrors(mapOf(NutrientField.SODIUM to "Enter a valid number")),
            calorieWarning = "Calories (165 kcal) don't quite match the macros (≈ 156 kcal). You can still save.",
            onCaloriesChanged = {},
            onProteinChanged = {},
            onCarbohydratesChanged = {},
            onTotalFatChanged = {},
            onFiberChanged = {},
            onSugarChanged = {},
            onSodiumChanged = {},
            onCholesterolChanged = {},
            onPotassiumChanged = {},
            onCalciumChanged = {},
            onIronChanged = {},
            onVitaminAChanged = {},
            onVitaminCChanged = {},
            onVitaminDChanged = {},
        )
    }
}
