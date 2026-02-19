package com.example.responsiveapp.presentation.food_database_screen.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.responsiveapp.domain.model.Food
import com.example.responsiveapp.presentation.ui.theme.spacing
import kotlin.math.roundToInt

@Composable
fun FoodCard(
    modifier: Modifier = Modifier,
    food: Food,
    onFoodClick: (String) -> Unit
) {
    val serving = food.defaultServing

    val calories = remember(serving) {
        serving?.nutrition?.calories?.roundToInt() ?: 0
    }

    val servingAmount = remember(serving) {
        serving?.amount?.roundToInt() ?: 0
    }

    val servingUnit = remember(serving) {
        serving?.unit?.name.orEmpty()
    }

    Surface(
        modifier = modifier.fillMaxWidth()
            .clickable{
                onFoodClick(food.id)
            },
        color = MaterialTheme.colorScheme.surface,
        shape = MaterialTheme.shapes.medium
    ) {

        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(MaterialTheme.spacing.md),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.sm),
            ) {
                Text(
                    text = food.name,
                    style = MaterialTheme.typography.titleMedium
                        .copy(
                        color = MaterialTheme.colorScheme.onSurface
                        )
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.xs),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = Icons.Default.LocalFireDepartment, contentDescription = null,
                        Modifier.size(18.dp))
                    Text(
                        text = " $calories cal - ",
                        style = MaterialTheme.typography.bodySmall
                            .copy(
                                color = MaterialTheme.colorScheme.onSurface
                            )
                    )
                    Text(text = "$servingAmount $servingUnit",
                        style = MaterialTheme.typography.bodySmall)
                }

            }

            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(32.dp)
            )
        }
    }

}