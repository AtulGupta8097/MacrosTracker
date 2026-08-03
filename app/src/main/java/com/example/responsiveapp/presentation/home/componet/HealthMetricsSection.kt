package com.example.responsiveapp.presentation.home.componet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.example.responsiveapp.domain.model.health.BmiStatus
import com.example.responsiveapp.presentation.home.model.HealthMetricsUiState
import com.example.responsiveapp.presentation.ui.theme.FitnessBlue
import com.example.responsiveapp.presentation.ui.theme.FitnessOrange
import com.example.responsiveapp.presentation.ui.theme.ResponsiveAppTheme
import com.example.responsiveapp.presentation.ui.theme.spacing

@Composable
fun HealthMetricsSection(
    modifier: Modifier = Modifier,
    healthMetricsUiState: HealthMetricsUiState,
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
        BmiCard(
            bmiText = healthMetricsUiState.bmiText,
            bmiStatus = healthMetricsUiState.bmiStatus,
            modifier = Modifier.fillMaxWidth(),
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.sm),
        ) {

            EnergyMetricCard(
                icon = Icons.Default.LocalFireDepartment,
                title = "TDEE",
                valueText = healthMetricsUiState.tdeeText,
                description = "Calories needed to maintain your current weight.",
                accentColor = FitnessOrange,
                modifier = Modifier.weight(1f),
            )

            EnergyMetricCard(
                icon = Icons.Default.Bolt,
                title = "BMR",
                valueText = healthMetricsUiState.bmrText,
                description = "Calories your body burns while at complete rest.",
                accentColor = FitnessBlue,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HealthMetricsSectionPreview() {
    ResponsiveAppTheme {
        HealthMetricsSection(
            healthMetricsUiState = HealthMetricsUiState(
                bmiText = "22.5",
                bmiStatus = BmiStatus.HEALTHY,
                tdeeText = "2340",
                bmrText = "1685",
            )
        )
    }
}