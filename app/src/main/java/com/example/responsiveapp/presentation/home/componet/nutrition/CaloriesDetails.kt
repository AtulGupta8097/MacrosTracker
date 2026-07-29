package com.example.responsiveapp.presentation.home.componet.nutrition

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.responsiveapp.presentation.commoncomponent.CustomButton
import com.example.responsiveapp.presentation.ui.theme.spacing

@Composable
fun CaloriesDetails(
    modifier: Modifier = Modifier,
    consumedCalories: Float,
    targetCalories: Int,
    progress: Float,
    onLogFoodClick: () -> Unit,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(
            MaterialTheme.spacing.sm,
        ),
    ) {

        CaloriesInfo(
            consumedCalories = consumedCalories,
            targetCalories = targetCalories,
        )

        LinearProgress(
            progress = progress,
        )

        CustomButton(
            modifier = Modifier.fillMaxWidth(),
            text = "Log Food",
            imageVector = Icons.Default.Add,
            onClick = onLogFoodClick,

        )
    }
}