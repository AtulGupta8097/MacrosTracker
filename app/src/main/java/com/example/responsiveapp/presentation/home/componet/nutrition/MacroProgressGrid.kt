package com.example.responsiveapp.presentation.home.componet.nutrition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.responsiveapp.presentation.home.model.MacroProgressUiModel
import com.example.responsiveapp.presentation.ui.theme.spacing

private val MinCardWidth = 156.dp

@Composable
fun MacroProgressGrid(
    macros: List<MacroProgressUiModel>,
    modifier: Modifier = Modifier,
) {
    BoxWithConstraints(
        modifier = modifier,
    ) {

        val spacing = MaterialTheme.spacing.sm

        val columnCount = remember(maxWidth, macros.size, spacing) {
            val perColumn = MinCardWidth + spacing
            val fitting = ((maxWidth + spacing) / perColumn).toInt()

            fitting.coerceIn(1, macros.size.coerceAtLeast(1))
        }


        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(spacing),
        ) {
            macros
                .chunked(columnCount)
                .forEach { rowMacros ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(spacing),
                    ) {
                        rowMacros.forEach { macro ->
                            MacroProgressCard(
                                macro = macro,
                                modifier = Modifier.weight(1f),
                            )
                        }

                        // It prevents the column to be of different sizes
                        repeat(columnCount - rowMacros.size) {
                            Spacer(
                                modifier = Modifier.weight(1f),
                            )
                        }
                    }
                }
        }


    }
}
