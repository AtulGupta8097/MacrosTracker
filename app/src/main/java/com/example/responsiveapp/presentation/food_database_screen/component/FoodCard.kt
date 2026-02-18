package com.example.responsiveapp.presentation.food_database_screen.component

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.responsiveapp.domain.model.Food
import com.example.responsiveapp.presentation.ui.theme.spacing

@Composable
fun FoodCard(
    food: Food,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        shape = MaterialTheme.shapes.small
    ) {
        val serving = food.defaultServing
        val calories = serving?.nutrition?.calories
        val servingDescription = serving?.description
        val servingUnit = serving?.unit?.name
        val servingAmount = serving?.amount
        Log.d("FoodCard", "FoodCard: $food")

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.sm)
            ) {
                Text(
                    text = food.name,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = calories?.toString() ?: "",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = servingDescription ?: "",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "$servingAmount $servingUnit",
                    style = MaterialTheme.typography.bodySmall
                )

            }
            Icon(imageVector = Icons.Default.Add, contentDescription = null)
        }
    }

}