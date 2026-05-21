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
import androidx.compose.material.icons.filled.Add
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
import com.example.responsiveapp.domain.model.mymeals.MyMeal
import com.example.responsiveapp.presentation.ui.theme.ResponsiveAppTheme

@Composable
fun MyMealCard(
    modifier: Modifier = Modifier,
    myMeal: MyMeal,
    isSelected: Boolean = false,
    onLog: () -> Unit = {},
    onEdit: () -> Unit
) {
    val ingredientCount = myMeal.ingredients.size

    Surface(
        modifier = modifier.fillMaxWidth(),
        onClick = onEdit,
        shape = MaterialTheme.shapes.medium,
        color = if (isSelected) MaterialTheme.colorScheme.primaryContainer
        else MaterialTheme.colorScheme.surface,
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 48.dp), // prevents overlap with button
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {

                Text(
                    text = myMeal.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Row(
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    Icon(
                        imageVector = Icons.Default.LocalFireDepartment,
                        contentDescription = "Calories",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(18.dp)
                    )

                    Column(
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {

                        Text(
                            text = "${myMeal.totalNutritionInfo.calories} cal",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Text(
                            text = "$ingredientCount ingredients",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
            
            Surface(
                onClick = onLog,
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(36.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Log",
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun PrevMyMealCard() {
    ResponsiveAppTheme {
        MyMealCard(
            myMeal = MyMeal(
                id = "1",
                name = "Pizza",
                ingredients = emptyList(),
                totalNutritionInfo = com.example.responsiveapp.domain.model.NutritionInfo(),
                createAt = System.currentTimeMillis()
            ),
            onEdit = {}
        )
    }
}