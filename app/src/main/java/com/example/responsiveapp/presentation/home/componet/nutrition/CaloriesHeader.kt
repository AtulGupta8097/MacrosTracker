package com.example.responsiveapp.presentation.home.componet.nutrition

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight

@Composable
fun CaloriesHeader(
    modifier: Modifier = Modifier,
) {
    Text(
        modifier = modifier,
        text = "Calories",
        style = MaterialTheme.typography.titleMedium
            .copy(
                fontWeight = FontWeight.Bold,
            ),
        color = MaterialTheme.colorScheme.onSurface,
    )
}
