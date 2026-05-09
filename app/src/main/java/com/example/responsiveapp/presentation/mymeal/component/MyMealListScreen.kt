package com.example.responsiveapp.presentation.mymeal.component

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
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
import androidx.compose.material.icons.outlined.RestaurantMenu
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.responsiveapp.domain.model.mymeals.MyMeal
import com.example.responsiveapp.presentation.commoncomponent.CustomButton
import com.example.responsiveapp.presentation.commoncomponent.EmptyState
import com.example.responsiveapp.presentation.ui.theme.spacing

@Composable
fun MyMealListScreen(
    modifier: Modifier = Modifier,
    meals: List<MyMeal> = emptyList(),
    onCreateClick: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        Box(
            modifier = Modifier.weight(1f)
        ) {
            androidx.compose.animation.AnimatedVisibility(
                visible = meals.isEmpty(),
                enter = fadeIn(tween(300)),
                exit = fadeOut(tween(200)),
                modifier = Modifier.align(Alignment.Center),
            ) {
                EmptyState(
                    title = "No meals yet",
                    description = "Tap + to build your first\ncustom meal from real foods.",
                    icon = Icons.Outlined.RestaurantMenu,
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            androidx.compose.animation.AnimatedVisibility(
                visible = meals.isNotEmpty(),
                enter = fadeIn(tween(300)) +
                        slideInVertically(tween(300)) { it / 8 },
                exit = fadeOut(tween(200)),
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(
                        horizontal = MaterialTheme.spacing.md,
                        vertical = MaterialTheme.spacing.md,
                    ),
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.sm),
                ) {
                    items(meals, key = { it.id }) { meal ->
                        MyMealCard(
                            myMeal = meal,
                            onLog = { }
                        )
                    }
                }
            }
        }
        CustomButton(
            text = "Create Meal",
            onClick = {
                onCreateClick()
            },
            imageVector = Icons.Default.Add,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(80.dp)
                .padding(MaterialTheme.spacing.md),
            buttonColors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        )
    }

}