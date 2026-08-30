package com.example.responsiveapp.presentation.progress.componet.macrosdistribution

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PieChart
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.responsiveapp.presentation.ui.theme.spacing

@Composable
fun MacroDistributionEmptyState(
    modifier: Modifier = Modifier,
) {
    val colorScheme = MaterialTheme.colorScheme
    val spacing = MaterialTheme.spacing

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = spacing.lg,
                vertical = spacing.xl,
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {

        // Chart visual
        Box(
            modifier = Modifier.size(96.dp),
            contentAlignment = Alignment.Center,
        ) {

            // Outer soft glow
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .background(
                        color = colorScheme.primary.copy(alpha = 0.045f),
                        shape = CircleShape,
                    ),
            )

            // Middle ring
            Box(
                modifier = Modifier
                    .size(76.dp)
                    .background(
                        color = colorScheme.primary.copy(alpha = 0.08f),
                        shape = CircleShape,
                    ),
            )

            // Icon container
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(
                        color = colorScheme.primary.copy(alpha = 0.14f),
                        shape = CircleShape,
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Outlined.PieChart,
                    contentDescription = null,
                    tint = colorScheme.primary,
                    modifier = Modifier.size(28.dp),
                )
            }
        }

        Spacer(modifier = Modifier.size(spacing.lg))

        Text(
            text = "Your macro story starts here",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
            ),
            color = colorScheme.onSurface,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.size(spacing.xs))

        Text(
            text = "Log a meal to see how your protein, carbs, and fats are distributed.",
            modifier = Modifier
                .fillMaxWidth()
                .alpha(0.9f),
            style = MaterialTheme.typography.bodySmall,
            color = colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
    }
}