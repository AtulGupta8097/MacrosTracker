package com.example.responsiveapp.presentation.myfood.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun StepDot(
    active: Boolean,
    label: String,
    aligned: Alignment.Horizontal,
) {
    Column(
        horizontalAlignment = aligned,
    ) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .clip(CircleShape)
                .background(
                    if (active) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                    }
                ),
        )

        Spacer(
            modifier = Modifier.height(4.dp),
        )

        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(
                color =
                    if (active) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    },
                fontWeight =
                    if (active) {
                        FontWeight.Bold
                    } else {
                        FontWeight.Normal
                    },
            ),
        )
    }
}