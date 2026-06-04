package com.example.responsiveapp.presentation.myfood.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.example.responsiveapp.presentation.ui.theme.spacing

@Composable
fun NutrientSectionHeader(
    title: String,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {

        Spacer(
            modifier = Modifier.height(
                MaterialTheme.spacing.sm,
            ),
        )

        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
            ),
        )

        Spacer(
            modifier = Modifier.height(
                MaterialTheme.spacing.xs,
            ),
        )

        HorizontalDivider(
            color = MaterialTheme.colorScheme.outlineVariant,
        )

        Spacer(
            modifier = Modifier.height(
                MaterialTheme.spacing.xs,
            ),
        )
    }
}