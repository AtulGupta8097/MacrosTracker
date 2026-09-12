package com.example.responsiveapp.presentation.usersetup.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.example.responsiveapp.presentation.ui.theme.spacing
import com.example.responsiveapp.presentation.usersetup.component.UnitSelector
import com.example.responsiveapp.presentation.usersetup.component.WeightPicker
import com.example.responsiveapp.presentation.usersetup.component.WeightUnit

@Composable
fun WeightScreen(
    weight: Float?,
    onWeightChanged: (Float) -> Unit
) {
    var selectedUnit by remember { mutableStateOf(WeightUnit.KG) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(MaterialTheme.colorScheme.background)
            .padding(MaterialTheme.spacing.md)
    ) {
        Text(
            text = "What's your Weight?",
            style = MaterialTheme.typography.displaySmall.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        )

        Text(
            text = "We'll use this to track your progress",
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
                weight = weight,
                onWeightChanged = onWeightChanged,
                minWeight = 30f,
                maxWeight = 200f,
                unit = "kg"
            )
            WeightUnit.LBS -> WeightPicker(
                weight = weight?.times(2.20462f),
                onWeightChanged = { onWeightChanged(it / 2.20462f) },
                minWeight = 66f,
                maxWeight = 440f,
                unit = "lbs"
            )
        }
    }
}