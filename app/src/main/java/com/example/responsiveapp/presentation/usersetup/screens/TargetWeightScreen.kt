package com.example.responsiveapp.presentation.usersetup.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingDown
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.Balance
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.responsiveapp.presentation.ui.theme.spacing
import com.example.responsiveapp.presentation.usersetup.component.UnitSelector
import com.example.responsiveapp.presentation.usersetup.component.WeightPicker
import com.example.responsiveapp.presentation.usersetup.component.WeightUnit
import kotlin.math.abs
import kotlin.math.roundToInt

@Composable
fun TargetWeightScreen(
    currentWeight: Float?,
    targetWeight: Float?,
    onTargetWeightChanged: (Float) -> Unit
) {
    var selectedUnit by remember { mutableStateOf(WeightUnit.KG) }
    val effectiveTarget = targetWeight ?: currentWeight

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(MaterialTheme.colorScheme.background)
            .padding(MaterialTheme.spacing.md)
    ) {
        Text(
            text = "What's your Target Weight?",
            style = MaterialTheme.typography.displaySmall.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        )

        Text(
            text = "We'll tailor your daily calories to help you get there",
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
            ),
            modifier = Modifier.padding(
                top = MaterialTheme.spacing.xs,
                bottom = MaterialTheme.spacing.lg
            )
        )

        UnitSelector(
            selectedUnit = selectedUnit,
            onUnitChanged = { selectedUnit = it }
        )

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.xl))

        when (selectedUnit) {
            WeightUnit.KG -> WeightPicker(
                weight = effectiveTarget,
                onWeightChanged = onTargetWeightChanged,
                minWeight = 30f,
                maxWeight = 200f,
                unit = "kg"
            )
            WeightUnit.LBS -> WeightPicker(
                weight = effectiveTarget?.times(2.20462f),
                onWeightChanged = { onTargetWeightChanged(it / 2.20462f) },
                minWeight = 66f,
                maxWeight = 440f,
                unit = "lbs"
            )
        }

        if (currentWeight != null && targetWeight != null) {
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.lg))
            WeightDeltaHint(currentWeight = currentWeight, targetWeight = targetWeight)
        }
    }
}

@Composable
private fun WeightDeltaHint(
    currentWeight: Float,
    targetWeight: Float
) {
    val delta = targetWeight - currentWeight
    val absDeltaText = String.format("%.1f", abs(delta))

    val (icon, message) = when {
        delta < -0.1f -> Icons.AutoMirrored.Filled.TrendingDown to "You're aiming to lose $absDeltaText kg"
        delta > 0.1f -> Icons.AutoMirrored.Filled.TrendingUp to "You're aiming to gain $absDeltaText kg"
        else -> Icons.Filled.Balance to "You're aiming to maintain your current weight"
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
                RoundedCornerShape(16.dp)
            )
            .padding(MaterialTheme.spacing.md),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.width(MaterialTheme.spacing.sm))
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
        )
    }
}