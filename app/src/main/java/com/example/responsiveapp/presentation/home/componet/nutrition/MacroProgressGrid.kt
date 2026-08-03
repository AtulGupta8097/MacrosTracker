package com.example.responsiveapp.presentation.home.componet.nutrition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.responsiveapp.presentation.home.model.MacroProgressUiModel
import com.example.responsiveapp.presentation.ui.theme.spacing

private val ExpandedMacroGridBreakpoint = 480.dp

@Composable
fun MacroProgressGrid(
    macros: List<MacroProgressUiModel>,
    modifier: Modifier = Modifier,
) {
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

                macros.forEach { macro ->

                    MacroProgressCard(
                        macro = macro,
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

                macros
                    .chunked(2)
                    .forEach { rowCards ->

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(
                                MaterialTheme.spacing.sm,
                            ),
                        ) {
                            rowCards.forEach { macro ->

                                MacroProgressCard(
                                    macro = macro,
                                    modifier = Modifier.weight(1f),
                                )
                            }
                        }
                    }
            }
        }
    }
}
