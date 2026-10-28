package com.example.responsiveapp.presentation.home.componet

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.example.responsiveapp.presentation.ui.theme.spacing

@Composable
fun DailyGreeting(
    modifier: Modifier = Modifier,
    name: String,
) {
    val greeting = if (name.isBlank()) {
        "Stronger every day!"
    } else {
        "Stronger every day, ${name.trim()}!"
    }

    Text(
        text = greeting,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = MaterialTheme.spacing.md,vertical = MaterialTheme.spacing.sm),
        style = MaterialTheme.typography.headlineMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onBackground,
    )
}