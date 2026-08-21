package com.example.responsiveapp.presentation.progress.componet.weight

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.MonitorWeight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.responsiveapp.presentation.progress.model.WeightProgressUiState
import com.example.responsiveapp.presentation.ui.theme.spacing

@Composable
fun WeightProgressSection(
    modifier: Modifier = Modifier,
    state: WeightProgressUiState,
    onLogWeightClicked: () -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(20.dp))
            .padding(MaterialTheme.spacing.md),
    ) {
        Column {
            WeightProgressHeader(
                logCount = state.logCount,
                onLogWeightClicked = onLogWeightClicked,
            )

            if (state.hasData) {
                key(state.points.size) {
                    WeightTrendChart(
                        points = state.points,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = MaterialTheme.spacing.lg),
                    )
                }
            } else {
                WeightEmptyState(
                    onLogWeightClicked = onLogWeightClicked,
                    modifier = Modifier.padding(top = MaterialTheme.spacing.md),
                )
            }
        }
    }
}

@Composable
private fun WeightProgressHeader(
    logCount: Int,
    onLogWeightClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val accentColor = MaterialTheme.colorScheme.primary

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .background(accentColor.copy(alpha = 0.16f), CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Outlined.MonitorWeight,
                contentDescription = null,
                tint = accentColor,
                modifier = Modifier.size(22.dp),
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = MaterialTheme.spacing.sm),
        ) {
            Text(
                text = "Weight Progress",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                text = if (logCount > 0) "$logCount logs" else "No logs yet",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        Button(
            onClick = onLogWeightClicked,
            contentPadding = PaddingValues(
                horizontal = MaterialTheme.spacing.sm,
                vertical = MaterialTheme.spacing.xs,
            ),
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(
                containerColor = accentColor,
                contentColor = MaterialTheme.colorScheme.onPrimary,
            ),
        ) {
            Icon(
                imageVector = Icons.Outlined.Add,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
            )
            Text(
                text = "Log Weight",
                modifier = Modifier.padding(start = MaterialTheme.spacing.xxs),
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold,
            )
        }
    }
}