package com.example.responsiveapp.presentation.mymeal.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.tooling.preview.Preview
import com.example.responsiveapp.core.utils.formatMacroValue
import com.example.responsiveapp.domain.model.FoodItem
import com.example.responsiveapp.domain.model.MacroSummary
import com.example.responsiveapp.presentation.ui.theme.ResponsiveAppTheme
import com.example.responsiveapp.presentation.ui.theme.spacing

@Composable
fun FoodSheetCard(
    food: FoodItem,
    onAdd: () -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        onClick = onAdd,
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surface,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 48.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = food.name,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                if (!food.brand.isNullOrBlank()) {
                    Text(
                        text = food.brand,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Text(
                    text = "${food.macroSummary.calories.toInt()} cal • " +
                            "P: ${formatMacroValue(food.macroSummary.protein)}g • " +
                            "C: ${formatMacroValue(food.macroSummary.carbs)}g • " +
                            "F: ${formatMacroValue(food.macroSummary.fat)}g",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    maxLines = 1,
                    softWrap = false
                )
            }

            Surface(
                onClick = onAdd,
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(36.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Add,
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