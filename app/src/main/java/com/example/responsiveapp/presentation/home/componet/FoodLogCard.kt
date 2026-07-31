package com.example.responsiveapp.presentation.home.componet

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Grain
import androidx.compose.material.icons.filled.Opacity
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.responsiveapp.core.utils.DateUtils
import com.example.responsiveapp.core.utils.formatMacroValue
import com.example.responsiveapp.domain.model.NutritionInfo
import com.example.responsiveapp.domain.model.foodlog.FoodLog
import com.example.responsiveapp.presentation.ui.theme.CaloriesColor
import com.example.responsiveapp.presentation.ui.theme.CarbsColor
import com.example.responsiveapp.presentation.ui.theme.FatColor
import com.example.responsiveapp.presentation.ui.theme.ProteinColor
import com.example.responsiveapp.presentation.ui.theme.ResponsiveAppTheme
import com.example.responsiveapp.presentation.ui.theme.spacing
@Composable
fun FoodLogCard(
    foodLog: FoodLog,
    modifier: Modifier = Modifier,
) {

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant,
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp,
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

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {

                Text(
                    text = foodLog.foodName,
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

                Text(
                    text = DateUtils.formatTimeOfDay(
                        foodLog.createdAt,
                    ),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(
                    MaterialTheme.spacing.xs,
                ),
            ) {

                Icon(
                    imageVector = Icons.Default.Bolt,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                    tint = CaloriesColor,
                )

                Text(
                    text = "${foodLog.nutrition.calories.toInt()} kcal",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(
                    MaterialTheme.spacing.lg,
                ),
            ) {

                MacroItem(
                    icon = Icons.Default.FitnessCenter,
                    value = formatMacroValue(foodLog.nutrition.protein),
                    tint = ProteinColor,
                )

                MacroItem(
                    icon = Icons.Default.Grain,
                    value = formatMacroValue(foodLog.nutrition.carbs),
                    tint = CarbsColor,
                )

                MacroItem(
                    icon = Icons.Default.Opacity,
                    value = formatMacroValue(foodLog.nutrition.fat),
                    tint = FatColor,
                )
            }
        }
    }
}

@Composable
private fun MacroItem(
    icon: ImageVector,
    value: String,
    tint: Color,
) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(
            4.dp,
        ),
    ) {

        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(18.dp),
            tint = tint,
        )

        Text(
            text = "${value}g",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = tint,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FoodLogCardPreview() {

    ResponsiveAppTheme {

        FoodLogCard(
            foodLog = FoodLog(
                id = "1",
                foodName = "Chicken Thigh",
                nutrition = NutritionInfo(
                    calories = 209f,
                    protein = 26f,
                    carbs = 0f,
                    fat = 11f,
                ),
                createdAt = System.currentTimeMillis(),
            ),
        )
    }
}