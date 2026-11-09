package com.macrivo.app.presentation.barcode.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.macrivo.app.domain.model.Serving
import com.macrivo.app.presentation.ui.theme.spacing

// CHANGED: restyled to match the quantity row - label on the left, the current serving as a
// tappable chip (with a dropdown arrow when there is more than one serving) on the right.
@Composable
fun ServingSelector(
    servings: List<Serving>,
    selectedServing: Serving,
    enabled: Boolean,
    onServingSelected: (Serving) -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }
    val canChoose = enabled && servings.size > 1

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = MaterialTheme.spacing.md, vertical = MaterialTheme.spacing.sm),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "Serving size",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f),
        )

        Box {
            Surface(
                onClick = { expanded = true },
                enabled = canChoose,
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
            ) {
                Row(
                    modifier = Modifier
                        .padding(horizontal = MaterialTheme.spacing.md, vertical = MaterialTheme.spacing.sm + MaterialTheme.spacing.xs)
                        .widthIn(max = 200.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = selectedServing.description,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, fill = false),
                    )
                    if (servings.size > 1) {
                        Icon(
                            imageVector = Icons.Filled.KeyboardArrowDown,
                            contentDescription = "Choose serving size",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }

            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                servings.forEach { serving ->
                    DropdownMenuItem(
                        text = { Text(serving.description) },
                        onClick = {
                            expanded = false
                            onServingSelected(serving)
                        },
                    )
                }
            }
        }
    }
}
