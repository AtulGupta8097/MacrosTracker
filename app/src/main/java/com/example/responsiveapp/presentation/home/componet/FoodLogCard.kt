package com.example.responsiveapp.presentation.home.componet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.responsiveapp.core.utils.DateUtils
import com.example.responsiveapp.core.utils.formatMacroValue
import com.example.responsiveapp.domain.model.NutritionInfo
import com.example.responsiveapp.domain.model.foodlog.FoodLog
import com.example.responsiveapp.presentation.ui.theme.ResponsiveAppTheme
import com.example.responsiveapp.presentation.ui.theme.spacing

@Composable
fun FoodLogCard(
    foodLog: FoodLog,
    modifier: Modifier = Modifier,
) {

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp,
        ),
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    MaterialTheme.spacing.sm,
                ),
            verticalAlignment = Alignment.CenterVertically,
        ) {

            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primary.copy(
                            alpha = 0.12f,
                        ),
                        shape = RoundedCornerShape(
                            MaterialTheme.spacing.xs,
                        ),
                    ),
                contentAlignment = Alignment.Center,
            ) {

                Icon(
                    imageVector = Icons.Default.Restaurant,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(
                        horizontal = MaterialTheme.spacing.sm,
                    ),
            ) {

                Text(
                    text = foodLog.foodName,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

                Text(
                    text = "${DateUtils.formatTimeOfDay(foodLog.createdAt)} · ${foodLog.macroSummary()}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }

            Column(
                horizontalAlignment = Alignment.End,
            ) {

                Text(
                    text = foodLog.nutrition.calories.toInt().toString(),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                )

                Text(
                    text = "kcal",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

private fun FoodLog.macroSummary(): String {

    val nutrition = nutrition

    return buildString {

        append("P ")
        append(formatMacroValue(nutrition.protein))
        append("g")

        append(" · ")

        append("C ")
        append(formatMacroValue(nutrition.carbs))
        append("g")

        append(" · ")

        append("F ")
        append(formatMacroValue(nutrition.fat))
        append("g")
    }
}

@Preview(showBackground = true)
@Composable
private fun FoodLogCardPreview() {

    ResponsiveAppTheme {

        FoodLogCard(
            foodLog = FoodLog(
                id = "1",
                foodName = "Grilled Chicken Breast",
                nutrition = NutritionInfo(
                    calories = 248f,
                    protein = 46.5f,
                    carbs = 0f,
                    fat = 5.4f,
                ),
                createdAt = System.currentTimeMillis(),
            ),
        )
    }
}