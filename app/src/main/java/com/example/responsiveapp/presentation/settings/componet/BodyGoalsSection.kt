package com.example.responsiveapp.presentation.settings.componet

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.MonitorWeight
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.TrackChanges
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.responsiveapp.presentation.settings.model.ProfileSummaryUiState
import com.example.responsiveapp.presentation.ui.theme.CarbsColor
import com.example.responsiveapp.presentation.ui.theme.spacing

@Composable
fun BodyGoalsSection(
    profile: ProfileSummaryUiState,
    onWeightClick: () -> Unit,
    onTargetWeightClick: () -> Unit,
    onGoalClick: () -> Unit,
    onActivityClick: () -> Unit,
    onBodyDetailsClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    SettingsSectionCard(modifier = modifier, label = "Body & Goals") {

        SettingsRow(
            title = "Current Weight",
            icon = Icons.Default.MonitorWeight,
            iconTint = CarbsColor,
            iconBackground = CarbsColor.copy(alpha = 0.14f),
            trailingText = profile.currentWeightText,
            onClick = onWeightClick,
        )

        RowDivider()

        SettingsRow(
            title = "Target Weight",
            icon = Icons.Default.TrackChanges,
            iconTint = MaterialTheme.colorScheme.primary,
            iconBackground = MaterialTheme.colorScheme.primaryContainer,
            trailingText = profile.targetWeightText,
            onClick = onTargetWeightClick,
        )

        RowDivider()

        SettingsRow(
            title = "Goal",
            icon = Icons.Default.Flag,
            iconTint = MaterialTheme.colorScheme.tertiary,
            iconBackground = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.14f),
            trailingText = profile.goal.label,
            onClick = onGoalClick,
        )

        RowDivider()

        SettingsRow(
            title = "Activity Level",
            icon = Icons.Default.Bolt,
            iconTint = MaterialTheme.colorScheme.secondaryContainer,
            iconBackground = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.14f),
            trailingText = profile.activityLevel.label,
            onClick = onActivityClick,
        )

        RowDivider()

        SettingsRow(
            title = "Height, Age & Gender",
            icon = Icons.Default.Person,
            trailingText = profile.bodyDetailsText,
            onClick = onBodyDetailsClick,
        )
    }
}

@Composable
private fun RowDivider() {
    HorizontalDivider(
        modifier = Modifier.padding(start = MaterialTheme.spacing.md + 34.dp + MaterialTheme.spacing.sm),
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.06f),
    )
}
