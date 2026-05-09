package com.example.responsiveapp.presentation.mymeal.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.responsiveapp.core.utils.formatMacroValue
import com.example.responsiveapp.domain.model.MealIngredient
import com.example.responsiveapp.presentation.ui.theme.ResponsiveAppTheme

@Composable
fun IngredientCard(
    ingredient: MealIngredient,
    onRemove: () -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        tonalElevation = 2.dp,
        color = MaterialTheme.colorScheme.surface
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 48.dp),        // prevents text overlapping the X button
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = ingredient.foodName,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                if (ingredient.servingDescription.isNotBlank()) {
                    Text(
                        text = "${formatMacroValue(ingredient.quantity)} × ${ingredient.servingDescription}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.LocalFireDepartment,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = "${ingredient.nutrition.calories.toInt()} cal",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    DotDivider()
                    Text(
                        text = "P: ${formatMacroValue(ingredient.nutrition.protein)}g",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    DotDivider()
                    Text(
                        text = "C: ${formatMacroValue(ingredient.nutrition.carbs)}g",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    DotDivider()
                    Text(
                        text = "F: ${formatMacroValue(ingredient.nutrition.fat)}g",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Surface(
                onClick = { onRemove() },
                shape = CircleShape,
                color = MaterialTheme.colorScheme.error.copy(alpha = 0.12f),
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(36.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Remove ingredient",
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun PrevIngredientCard() {
    ResponsiveAppTheme(
        darkTheme = false
    ) {
        IngredientCard(
            ingredient = MealIngredient(
                foodId = "1334",
                foodName = "Pizza",
                nutrition = com.example.responsiveapp.domain.model.NutritionInfo(),
                quantity = 1f
            ),
            onRemove = {}
        )
    }
}


