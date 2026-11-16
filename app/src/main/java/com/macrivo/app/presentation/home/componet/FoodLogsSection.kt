package com.macrivo.app.presentation.home.componet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.macrivo.app.presentation.commoncomponent.ConfirmDeleteFoodDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.macrivo.app.presentation.commoncomponent.FoodLogCard
import com.macrivo.app.presentation.home.model.FoodLogUiModel
import com.macrivo.app.presentation.home.model.RecentMealsUiState
import com.macrivo.app.presentation.ui.theme.ResponsiveAppTheme
import com.macrivo.app.presentation.ui.theme.spacing

@Composable
fun FoodLogsSection(
    recentMealsUiState: RecentMealsUiState,
    modifier: Modifier = Modifier,
    onMealClick: (String) -> Unit = {},
    onMealDelete: (String) -> Unit = {},
) {
    var pendingDelete by remember { mutableStateOf<FoodLogUiModel?>(null) }

    pendingDelete?.let { meal ->
        ConfirmDeleteFoodDialog(
            foodName = meal.name,
            onConfirm = {
                onMealDelete(meal.id)
                pendingDelete = null
            },
            onDismiss = { pendingDelete = null },
        )
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = MaterialTheme.spacing.md),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.sm),
    ) {

        Text(
            text = "Recently Logged",
            style = MaterialTheme.typography.titleMedium
                .copy(
                    fontWeight = FontWeight.Bold,
                ),
            color = MaterialTheme.colorScheme.onSurface,
        )

        if (recentMealsUiState.isEmpty) {
            EmptyFoodLogsState()
        } else {
            recentMealsUiState.meals.forEach { meal ->
                key(meal.id) {
                    SwipeToDeleteBox(
                        enabled = meal.canDelete,
                        onDeleteRequested = { pendingDelete = meal },
                    ) {
                        FoodLogCard(
                            name = meal.name,
                            caloriesText = meal.caloriesText,
                            proteinText = meal.proteinText,
                            carbsText = meal.carbsText,
                            fatText = meal.fatText,
                            onClick = { onMealClick(meal.id) },
                            trailingContent = {
                                Text(
                                    text = meal.timeText,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            },
                        )
                    }
                }
            }
        }
    }
}


@Composable
private fun SwipeToDeleteBox(
    enabled: Boolean,
    onDeleteRequested: () -> Unit,
    content: @Composable () -> Unit,
) {
    if (!enabled) {
        content()
        return
    }

    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { target ->
            if (target == SwipeToDismissBoxValue.EndToStart) onDeleteRequested()
            false // never stay dismissed - the item is only removed after the user confirms
        },
    )

    SwipeToDismissBox(
        state = dismissState,
        enableDismissFromStartToEnd = false,
        backgroundContent = {
            val swiping = dismissState.dismissDirection == SwipeToDismissBoxValue.EndToStart
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        if (swiping) MaterialTheme.colorScheme.error else Color.Transparent
                    )
                    .padding(horizontal = MaterialTheme.spacing.lg),
                contentAlignment = Alignment.CenterEnd,
            ) {
                if (swiping) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = MaterialTheme.colorScheme.onError,
                        )
                        Text(
                            text = "Delete",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onError,
                        )
                    }
                }
            }
        },
        content = { content() },
    )
}

@Preview(showBackground = true)
@Composable
private fun PrevFoodLogsSection() {
    ResponsiveAppTheme {
        FoodLogsSection(
            recentMealsUiState = RecentMealsUiState(
                meals = listOf(
                    FoodLogUiModel(
                        id = "1",
                        name = "Grilled chicken breast",
                        timeText = "1:15 PM",
                        caloriesText = "248 kcal",
                        proteinText = "46.5g",
                        carbsText = "0g",
                        fatText = "5.4g",
                    ),
                    FoodLogUiModel(
                        id = "2",
                        name = "Brown rice bowl",
                        timeText = "2:05 PM",
                        caloriesText = "320 kcal",
                        proteinText = "8g",
                        carbsText = "62g",
                        fatText = "4g",
                    ),
                ),
            ),
        )
    }
}