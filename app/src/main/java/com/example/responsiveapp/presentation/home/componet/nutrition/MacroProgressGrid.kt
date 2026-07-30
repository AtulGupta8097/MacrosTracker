package com.example.responsiveapp.presentation.home.componet.nutrition

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Grain
import androidx.compose.material.icons.filled.Opacity
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.responsiveapp.domain.model.NutritionProgress
import com.example.responsiveapp.domain.model.NutritionTargets
import com.example.responsiveapp.presentation.ui.theme.CarbsColor
import com.example.responsiveapp.presentation.ui.theme.FatColor
import com.example.responsiveapp.presentation.ui.theme.FiberColor
import com.example.responsiveapp.presentation.ui.theme.ProteinColor
import com.example.responsiveapp.presentation.ui.theme.spacing

private val ExpandedMacroGridBreakpoint = 480.dp

private data class MacroCardUiModel(
    val icon: ImageVector,
    val label: String,
    val consumedGrams: Float,
    val targetGrams: Int,
    val accentColor: Color,
)

@Composable
fun MacroProgressGrid(
    target: NutritionTargets,
    consumed: NutritionProgress,
    modifier: Modifier = Modifier,
) {
    val macroCards = listOf(
        MacroCardUiModel(
            icon = Icons.Default.FitnessCenter,
            label = "Protein",
            consumedGrams = consumed.protein,
            targetGrams = target.protein,
            accentColor = ProteinColor,
        ),
        MacroCardUiModel(
            icon = Icons.Default.Grain,
            label = "Carbs",
            consumedGrams = consumed.carbs,
            targetGrams = target.carbs,
            accentColor = CarbsColor,
        ),
        MacroCardUiModel(
            icon = Icons.Default.Opacity,
            label = "Fats",
            consumedGrams = consumed.fats,
            targetGrams = target.fats,
            accentColor = FatColor,
        ),
        MacroCardUiModel(
            icon = Icons.Default.Eco,
            label = "Fiber",
            consumedGrams = consumed.fiber,
            targetGrams = target.fiber,
            accentColor = FiberColor,
        ),
    )

    BoxWithConstraints(
        modifier = modifier,
    ) {

        val useExpandedLayout =
            maxWidth >= ExpandedMacroGridBreakpoint

        if (useExpandedLayout) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(
                    MaterialTheme.spacing.sm,
                ),
            ) {

                macroCards.forEach { card ->

                    MacroProgressCard(
                        icon = card.icon,
                        label = card.label,
                        consumedGrams = card.consumedGrams,
                        targetGrams = card.targetGrams,
                        accentColor = card.accentColor,
                        modifier = Modifier.weight(1f),
                    )
                }
            }

        } else {

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(
                    MaterialTheme.spacing.sm,
                ),
            ) {

                macroCards
                    .chunked(2)
                    .forEach { rowCards ->

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(
                                MaterialTheme.spacing.sm,
                            ),
                        ) {
                            rowCards.forEach { card ->

                                MacroProgressCard(
                                    icon = card.icon,
                                    label = card.label,
                                    consumedGrams = card.consumedGrams,
                                    targetGrams = card.targetGrams,
                                    accentColor = card.accentColor,
                                    modifier = Modifier.weight(1f),
                                )
                            }
                        }
                    }
            }
        }
    }
}
