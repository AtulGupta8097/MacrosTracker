package com.example.responsiveapp.presentation.progress.componet

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.example.responsiveapp.presentation.ui.theme.spacing

@Composable
fun ProgressHeader(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = MaterialTheme.spacing.md)
    ) {
        Text(
            modifier = Modifier.statusBarsPadding(),
            text = "Progress",
            style = MaterialTheme.typography.headlineLarge
                .copy(
                    fontWeight = FontWeight.Bold
                ),
            color = MaterialTheme.colorScheme.onBackground,
        )
        Text(
            text = "Track your nutrition, weight & consistency",
            modifier = Modifier.paddingFromBaseline(top = MaterialTheme.spacing.sm),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}
