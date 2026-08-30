package com.example.responsiveapp.presentation.progress.componet.weight

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.MonitorWeight
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
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
    val colorScheme = MaterialTheme.colorScheme
    val spacing = MaterialTheme.spacing

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .background(
                    color = colorScheme.primary.copy(alpha = 0.10f),
                    shape = CircleShape,
                ),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Outlined.MonitorWeight,
                contentDescription = null,
                tint = colorScheme.primary,
                modifier = Modifier.size(21.dp),
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = spacing.sm),
        ) {
            Text(
                text = "Weight Progress",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                ),
                color = colorScheme.onSurface,
            )

            Spacer(modifier = Modifier.size(2.dp))

            Text(
                text = when (logCount) {
                    0 -> "Start your journey"
                    else -> "$logCount entries"
                },
                maxLines = 1,
                style = MaterialTheme.typography.bodySmall,
                color = colorScheme.onSurfaceVariant,
            )
        }

        Spacer(modifier = Modifier.size(spacing.xs))

        FilledIconButton(
            onClick = onLogWeightClicked,
            modifier = Modifier.size(44.dp),
            shape = RoundedCornerShape(14.dp),
            colors = IconButtonDefaults.filledIconButtonColors(
                containerColor = colorScheme.primary,
                contentColor = colorScheme.onPrimary,
            ),
        ) {
            Icon(
                imageVector = Icons.Outlined.Add,
                contentDescription = "Log weight",
                modifier = Modifier.size(21.dp),
            )
        }
    }
}
