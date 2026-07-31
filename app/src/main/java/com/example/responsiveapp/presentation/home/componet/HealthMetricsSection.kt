package com.example.responsiveapp.presentation.home.componet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.responsiveapp.domain.model.NutritionTargets
import com.example.responsiveapp.domain.model.macros.MacroTarget
import com.example.responsiveapp.presentation.ui.theme.CarbsColor
import com.example.responsiveapp.presentation.ui.theme.FatColor
import com.example.responsiveapp.presentation.ui.theme.ProteinColor
import com.example.responsiveapp.presentation.ui.theme.ResponsiveAppTheme
import com.example.responsiveapp.presentation.ui.theme.spacing

@Composable
fun HealthMetricsSection(
    macroTarget: MacroTarget?,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = MaterialTheme.spacing.md),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.sm),
    ) {

        Text(
            text = "Health Metrics",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface,
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.large,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 1.dp,
            ),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(MaterialTheme.spacing.md),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {

                HealthMetricItem(
                    icon = Icons.Default.LocalFireDepartment,
                    label = "BMR",
                    value = macroTarget?.bmr?.toString() ?: "—",
                    unit = "kcal",
                    accentColor = FatColor,
                    modifier = Modifier.weight(1f),
                )

                HealthMetricItem(
                    icon = Icons.Default.Bolt,
                    label = "TDEE",
                    value = macroTarget?.tdee?.toString() ?: "—",
                    unit = "kcal",
                    accentColor = CarbsColor,
                    modifier = Modifier.weight(1f),
                )

                HealthMetricItem(
                    icon = Icons.Default.Flag,
                    label = "Daily Goal",
                    value = macroTarget?.targets?.calories?.toString() ?: "—",
                    unit = "kcal",
                    accentColor = ProteinColor,
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}

@Composable
private fun HealthMetricItem(
    icon: ImageVector,
    label: String,
    value: String,
    unit: String,
    accentColor: Color,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Box(
            modifier = Modifier
                .size(40.dp)
                .background(
                    color = accentColor.copy(alpha = 0.12f),
                    shape = CircleShape,
                ),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = accentColor,
                modifier = Modifier.size(20.dp),
            )
        }

        Text(
            text = value,
            modifier = Modifier.padding(
                top = MaterialTheme.spacing.xs,
            ),
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
        )

        Text(
            text = "$label · $unit",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PrevHealthMetricsSection() {
    ResponsiveAppTheme {
        HealthMetricsSection(
            macroTarget = MacroTarget(
                id = "1",
                targets = NutritionTargets(
                    calories = 1863,
                    protein = 140,
                    carbs = 220,
                    fats = 70,
                    fiber = 30,
                ),
                bmr = 1540,
                tdee = 1980,
                createdAt = 0L,
                updatedAt = 0L,
            ),
        )
    }
}