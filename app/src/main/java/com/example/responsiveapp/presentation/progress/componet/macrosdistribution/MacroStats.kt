package com.example.responsiveapp.presentation.progress.componet.macrosdistribution

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.responsiveapp.presentation.progress.model.MacroDistributionUiState
import com.example.responsiveapp.presentation.ui.theme.CarbsColor
import com.example.responsiveapp.presentation.ui.theme.FatColor
import com.example.responsiveapp.presentation.ui.theme.ProteinColor
import com.example.responsiveapp.presentation.ui.theme.spacing

@Composable
fun MacroStats(
    state: MacroDistributionUiState,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.background,
                shape = RoundedCornerShape(16.dp),
            )
            .padding(
                horizontal = MaterialTheme.spacing.md,
                vertical = MaterialTheme.spacing.sm,
            ),
    ) {
        MacroStatCell(
            letter = "P",
            grams = state.protein.grams,
            color = ProteinColor,
            modifier = Modifier.weight(1f),
        )

        MacroStatCell(
            letter = "C",
            grams = state.carbs.grams,
            color = CarbsColor,
            modifier = Modifier.weight(1f),
        )

        MacroStatCell(
            letter = "F",
            grams = state.fats.grams,
            color = FatColor,
            modifier = Modifier.weight(1f),
        )
    }
}