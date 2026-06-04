package com.example.responsiveapp.presentation.myfood.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.responsiveapp.presentation.myfood.CreateFoodStep
import com.example.responsiveapp.presentation.ui.theme.spacing

@Composable
fun StepIndicator(
    currentStep : CreateFoodStep,
    modifier    : Modifier = Modifier,
) {
    val onBasic     = currentStep == CreateFoodStep.BASIC_INFO
    val onNutrients = currentStep == CreateFoodStep.NUTRIENTS

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            .padding(4.dp),
    ) {
        Row(
            modifier            = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            StepPill(
                label    = "1  Basic Info",
                active   = onBasic,
                modifier = Modifier.weight(1f),
            )
            StepPill(
                label    = "2  Nutrients",
                active   = onNutrients,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun StepPill(
    label    : String,
    active   : Boolean,
    modifier : Modifier = Modifier,
) {
    Box(
        modifier         = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(
                if (active) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.surface.copy(alpha = 0f),
            )
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text  = label,
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = if (active) FontWeight.Bold else FontWeight.Normal,
                color      = if (active) MaterialTheme.colorScheme.onPrimary
                else MaterialTheme.colorScheme.onSurfaceVariant,
            ),
        )
    }
}