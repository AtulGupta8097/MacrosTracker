package com.example.responsiveapp.presentation.myfood.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.responsiveapp.presentation.commoncomponent.CustomButton
import com.example.responsiveapp.presentation.myfood.MacronutrientsState
import com.example.responsiveapp.presentation.myfood.MineralsState
import com.example.responsiveapp.presentation.myfood.VitaminsState
import com.example.responsiveapp.presentation.ui.theme.CaloriesColor
import com.example.responsiveapp.presentation.ui.theme.CarbsColor
import com.example.responsiveapp.presentation.ui.theme.FatColor
import com.example.responsiveapp.presentation.ui.theme.ProteinColor
import com.example.responsiveapp.presentation.ui.theme.ResponsiveAppTheme
import com.example.responsiveapp.presentation.ui.theme.spacing

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
    servingSize: String,
    macros: MacronutrientsState,
    minerals: MineralsState,
    vitamins: VitaminsState,
    canSave: Boolean,
    isEditMode: Boolean,
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
    onSave: () -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(MaterialTheme.spacing.md),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.md),
    ) {
        if (foodName.isNotBlank()) {
            item {
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
                        text = "Serving: $servingSize",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }

        item {
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
                )

                NutrientInputRow(
                    label = "Protein",
                    unit = "g",
                    value = macros.protein,
                    color = ProteinColor,
                    onChange = onProteinChanged,
                )

                NutrientInputRow(
                    label = "Carbohydrates",
                    unit = "g",
                    value = macros.carbohydrates,
                    color = CarbsColor,
                    onChange = onCarbohydratesChanged,
                )

                NutrientInputRow(
                    label = "Total Fat",
                    unit = "g",
                    value = macros.totalFat,
                    color = FatColor,
                    onChange = onTotalFatChanged,
                )

                NutrientInputRow(
                    label = "Fiber",
                    unit = "g",
                    value = macros.fiber,
                    color = FiberColor,
                    onChange = onFiberChanged,
                )

                NutrientInputRow(
                    label = "Sugar",
                    unit = "g",
                    value = macros.sugar,
                    color = SugarColor,
                    onChange = onSugarChanged,
                )
            }
        }

        item {
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
                )

                NutrientInputRow(
                    label = "Cholesterol",
                    unit = "mg",
                    value = minerals.cholesterol,
                    color = CholesterolColor,
                    onChange = onCholesterolChanged,
                )

                NutrientInputRow(
                    label = "Potassium",
                    unit = "mg",
                    value = minerals.potassium,
                    color = PotassiumColor,
                    onChange = onPotassiumChanged,
                )

                NutrientInputRow(
                    label = "Calcium",
                    unit = "mg",
                    value = minerals.calcium,
                    color = CalciumColor,
                    onChange = onCalciumChanged,
                )

                NutrientInputRow(
                    label = "Iron",
                    unit = "mg",
                    value = minerals.iron,
                    color = IronColor,
                    onChange = onIronChanged,
                )
            }
        }

        item {
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
                )

                NutrientInputRow(
                    label = "Vitamin C",
                    unit = "mg",
                    value = vitamins.vitaminC,
                    color = VitaminCColor,
                    onChange = onVitaminCChanged,
                )

                NutrientInputRow(
                    label = "Vitamin D",
                    unit = "mcg",
                    value = vitamins.vitaminD,
                    color = VitaminDColor,
                    onChange = onVitaminDChanged,
                )
            }
        }

        item {
            Spacer(
                modifier = Modifier.height(MaterialTheme.spacing.sm),
            )

            CustomButton(
                text = if (isEditMode) "Update Food" else "Save Food",
                onClick = onSave,
                enabled = canSave,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                buttonColors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    disabledContainerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
                    disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
                ),
            )
        }
    }
}

@Preview
@Composable
private fun PrevNutrientsStep() {
    ResponsiveAppTheme {
        NutrientsStep(
            foodName = "Grilled Chicken Breast",
            servingSize = "100g",
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
            canSave = true,
            isEditMode = false,
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
            onSave = {},
        )
    }
}