package com.example.responsiveapp.presentation.progress.componet.macrosdistribution

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.responsiveapp.presentation.ui.theme.CarbsColor
import com.example.responsiveapp.presentation.ui.theme.FatColor
import com.example.responsiveapp.presentation.ui.theme.ProteinColor
import com.example.responsiveapp.presentation.ui.theme.spacing

@Composable
fun MacroLegend(
    proteinGram: Int,
    carbsGram: Int,
    fatGram: Int,
    compact: Boolean,
    modifier: Modifier = Modifier,
) {
    val items = listOf(
        Triple("Protein", proteinGram, ProteinColor),
        Triple("Carbs", carbsGram, CarbsColor),
        Triple("Fat", fatGram, FatColor),
    )

    if (compact) {
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            items.forEach { (label, grams, color) ->
                MacroLegendRow(
                    label = label,
                    grams = grams,
                    compact = true,
                    color = color,
                )
            }
        }
    } else {
        Column(
            modifier = modifier.padding(vertical = MaterialTheme.spacing.md),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            items.forEach { (label, grams, color) ->
                MacroLegendRow(
                    label = label,
                    grams = grams,
                    compact = false,
                    color = color,
                )
            }
        }
    }
}