package com.example.responsiveapp.presentation.barcode

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.responsiveapp.domain.model.Serving
import com.example.responsiveapp.presentation.ui.theme.CarbsColor
import com.example.responsiveapp.presentation.ui.theme.FatColor
import com.example.responsiveapp.presentation.ui.theme.ProteinColor
import com.example.responsiveapp.presentation.ui.theme.spacing
import kotlin.math.roundToInt

@Composable
fun BarcodeScannerResultContent(
    state: BarcodeScanUiState.Found,
    onServingSelected: (Serving) -> Unit,
    onQuantityChanged: (Float) -> Unit,
    onAddToLog: () -> Unit,
    onScanAgain: () -> Unit,
    modifier: Modifier = Modifier,
) {
    BarcodeScannerSheet(modifier = modifier) {
        Column(verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.md)) {

            Row(verticalAlignment = Alignment.Top) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = state.foodName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        text = listOfNotNull(state.foodBrand, state.selectedServing.description)
                            .joinToString(" · "),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }

                Spacer(modifier = Modifier.width(MaterialTheme.spacing.md))

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "${state.scaledNutrition.calories.roundToInt()}",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                    )
                    Text(
                        text = "kcal",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }

            if (state.servings.size > 1) {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.sm)) {
                    items(state.servings, key = { it.id }) { serving ->
                        FilterChip(
                            selected = serving.id == state.selectedServing.id,
                            onClick = { onServingSelected(serving) },
                            label = { Text(serving.description, maxLines = 1) },
                        )
                    }
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.sm)) {
                MacroChip("Protein", state.scaledNutrition.protein, ProteinColor, Modifier.weight(1f))
                MacroChip("Carbs", state.scaledNutrition.carbs, CarbsColor, Modifier.weight(1f))
                MacroChip("Fat", state.scaledNutrition.fat, FatColor, Modifier.weight(1f))
            }

            QuantityStepper(
                quantity = state.quantity,
                onQuantityChanged = onQuantityChanged,
            )

            Row(horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.sm)) {
                OutlinedButton(
                    onClick = onScanAgain,
                    enabled = !state.isAdding,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(14.dp),
                ) {
                    Text("Scan again")
                }
                Button(
                    onClick = onAddToLog,
                    enabled = !state.isAdding,
                    modifier = Modifier.weight(2f),
                    shape = RoundedCornerShape(14.dp),
                ) {
                    if (state.isAdding) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            strokeWidth = 2.dp,
                            color = MaterialTheme.colorScheme.onPrimary,
                        )
                    } else {
                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(6.dp))
                        Text("Add to log", fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }
}

@Composable
private fun QuantityStepper(
    quantity: Float,
    onQuantityChanged: (Float) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = MaterialTheme.spacing.md, vertical = MaterialTheme.spacing.xs),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "Servings",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { onQuantityChanged(quantity - QuantityStep) }) {
                Icon(Icons.Default.Remove, contentDescription = "Decrease servings")
            }
            Text(
                text = formatQuantity(quantity),
                modifier = Modifier.widthIn(min = 40.dp),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
            )
            IconButton(onClick = { onQuantityChanged(quantity + QuantityStep) }) {
                Icon(Icons.Default.Add, contentDescription = "Increase servings")
            }
        }
    }
}

@Composable
private fun MacroChip(label: String, grams: Float, color: Color, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(MaterialTheme.colorScheme.background)
            .padding(vertical = MaterialTheme.spacing.sm),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = label.uppercase(),
            color = color,
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.labelSmall,
        )
        Text(
            text = "${grams.roundToInt()}g",
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

private const val QuantityStep = 0.5f

// CHANGED: "1.0" -> "1", "1.5" stays "1.5".
internal fun formatQuantity(quantity: Float): String =
    if (quantity % 1f == 0f) quantity.toInt().toString() else "%.1f".format(quantity)
