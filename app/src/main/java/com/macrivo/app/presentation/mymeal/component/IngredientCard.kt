package com.macrivo.app.presentation.mymeal.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.macrivo.app.core.utils.formatMacroValue
import com.macrivo.app.domain.model.MealIngredient
import com.macrivo.app.presentation.ui.theme.ResponsiveAppTheme
import com.macrivo.app.presentation.ui.theme.spacing

@Composable
fun IngredientCard(
    ingredient: MealIngredient,
    onRemove: () -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surface
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.spacing.md)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 48.dp),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.sm)
            ) {
                Text(
                    text = ingredient.foodName,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                if (ingredient.servingDescription.isNotBlank()) {
                    Text(
                        text = "${formatMacroValue(ingredient.quantity)} × ${ingredient.servingDescription}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Text(
                    text = "${ingredient.nutrition.calories.toInt()} cal •  " +
                            "P: ${formatMacroValue(ingredient.nutrition.protein)}g • " +
                            "C: ${formatMacroValue(ingredient.nutrition.carbs)}g • " +
                            "F: ${formatMacroValue(ingredient.nutrition.fat)}g",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    maxLines = 1,
                    softWrap = false
                )
            }

            Surface(
                onClick = onRemove,
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(36.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Add food",
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.padding(MaterialTheme.spacing.sm)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun PrevIngredientCard() {
    ResponsiveAppTheme(darkTheme = false) {
        IngredientCard(
            ingredient = MealIngredient(
                foodId = "1334",
                foodName = "Pizza",
                nutrition = com.macrivo.app.domain.model.NutritionInfo(
                    calories = 276f,
                    protein = 12.3f,
                    carbs = 30.3f,
                    fat = 11.2f
                ),
                quantity = 1f
            ),
            onRemove = {}
        )
    }
}