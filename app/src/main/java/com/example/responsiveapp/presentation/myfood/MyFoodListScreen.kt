package com.example.responsiveapp.presentation.myfood

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.FoodBank
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.responsiveapp.domain.model.myfood.CustomFood
import com.example.responsiveapp.presentation.commoncomponent.CustomButton
import com.example.responsiveapp.presentation.commoncomponent.EmptyState
import com.example.responsiveapp.presentation.myfood.component.MyFoodCard
import com.example.responsiveapp.presentation.ui.theme.spacing

@Composable
fun MyFoodListScreen(
    modifier: Modifier = Modifier,
    foods: List<CustomFood> = emptyList(),
    selectedFoodId: String? = null,
    onCreateFoodClick: () -> Unit,
    onFoodCardClick: (CustomFood) -> Unit,
) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(
                MaterialTheme.colorScheme.background
            ),
    ) {

        Box(
            modifier = Modifier.weight(1f)
        ) {

            androidx.compose.animation.AnimatedVisibility(
                visible = foods.isEmpty(),
            ) {

                EmptyState(
                    title =
                        "No custom foods yet",
                    description =
                        "Tap + to create your first\ncustom food with full nutrition info.",
                    icon =
                        Icons.Outlined.FoodBank,
                    tint =
                        MaterialTheme.colorScheme.primary,
                )
            }

            androidx.compose.animation.AnimatedVisibility(visible = foods.isNotEmpty()) {

                LazyColumn(
                    modifier =
                        Modifier.fillMaxSize(),
                    contentPadding =
                        PaddingValues(
                            horizontal =
                                MaterialTheme.spacing.md,
                            vertical =
                                MaterialTheme.spacing.md,
                        ),
                    verticalArrangement =
                        Arrangement.spacedBy(
                            MaterialTheme.spacing.sm
                        ),
                ) {

                    items(
                        items = foods,
                        key = { it.id },
                    ) { food ->

                        MyFoodCard(
                            food = food,
                            isSelected =
                                food.id == selectedFoodId,
                            onClick = {
                                onFoodCardClick(food)
                            },
                        )
                    }
                }
            }
        }

        CustomButton(
            text = "Create Custom Food",
            onClick = onCreateFoodClick,
            imageVector = Icons.Default.Add,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(80.dp)
                .padding(
                    MaterialTheme.spacing.md
                ),
            buttonColors =
                ButtonDefaults.buttonColors(
                    containerColor =
                        MaterialTheme.colorScheme.primary,
                    contentColor =
                        MaterialTheme.colorScheme.onPrimary,
                ),
        )
    }
}