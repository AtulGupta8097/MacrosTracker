package com.macrivo.app.presentation.barcode.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.macrivo.app.core.utils.formatMacroValue
import com.macrivo.app.domain.model.FoodDetail
import com.macrivo.app.domain.model.NutritionInfo
import com.macrivo.app.domain.model.Serving
import com.macrivo.app.presentation.ui.theme.CarbsColor
import com.macrivo.app.presentation.ui.theme.FatColor
import com.macrivo.app.presentation.ui.theme.FiberColor
import com.macrivo.app.presentation.ui.theme.ProteinColor
import com.macrivo.app.presentation.ui.theme.spacing

// CHANGED: redesigned. The screen is now two parts: a weighted, scrollable nutrition area and a
// dedicated bottom action bar that stays anchored (above the system navigation bar) no matter how
// much content there is. Nutrition values and calculations are unchanged - they are still passed
// in already scaled by quantity.
private val ContentMaxWidth: Dp = 600.dp

@Composable
fun ProductNutritionContent(
    food: FoodDetail,
    selectedServing: Serving,
    quantity: Float,
    nutrition: NutritionInfo,
    isLogging: Boolean,
    onServingSelected: (Serving) -> Unit,
    onQuantityIncrement: () -> Unit,
    onQuantityDecrement: () -> Unit,
    onLogFood: () -> Unit,
    onScanAnother: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val perLabel = if (quantity == 1f) {
        selectedServing.description
    } else {
        "${formatMacroValue(quantity)} \u00d7 ${selectedServing.description}"
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Scrollable content: takes all remaining height, so the actions below never move.
        Column(
            modifier = Modifier
                .weight(1f)
                .widthIn(max = ContentMaxWidth)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = MaterialTheme.spacing.md)
                .padding(top = MaterialTheme.spacing.sm, bottom = MaterialTheme.spacing.md),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.md),
        ) {
            FoodTitle(name = food.name, brand = food.brand)

            CalorieSummary(calories = nutrition.calories, perLabel = perLabel)

            MacroRow(nutrition = nutrition)

            SecondaryNutrients(nutrition = nutrition)

            SectionTitle("Log to Diary")

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.large,
                color = MaterialTheme.colorScheme.surface,
            ) {
                Column {
                    ServingSelector(
                        servings = food.servings,
                        selectedServing = selectedServing,
                        enabled = !isLogging,
                        onServingSelected = onServingSelected,
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = MaterialTheme.spacing.md),
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.15f),
                    )
                    QuantitySelector(
                        quantity = quantity,
                        caption = "\u00d7 ${selectedServing.description}",
                        enabled = !isLogging,
                        onIncrement = onQuantityIncrement,
                        onDecrement = onQuantityDecrement,
                    )
                }
            }
        }

        // Bottom action bar: separate from the scroll area, inset for the navigation bar.
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 3.dp,
            shadowElevation = 8.dp,
        ) {
            Box(contentAlignment = Alignment.TopCenter) {
                ScannerBottomActions(
                    primaryLabel = "Log Food",
                    primaryIcon = Icons.Filled.Add,
                    onPrimary = onLogFood,
                    isPrimaryLoading = isLogging,
                    enabled = !isLogging,
                    onScanAnother = onScanAnother,
                    modifier = Modifier
                        .widthIn(max = ContentMaxWidth)
                        .navigationBarsPadding()
                        .padding(MaterialTheme.spacing.md),
                )
            }
        }
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
        color = MaterialTheme.colorScheme.onBackground,
    )
}

@Composable
private fun FoodTitle(
    name: String,
    brand: String?,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.md),
    ) {
        Surface(
            modifier = Modifier.size(56.dp),
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Filled.Fastfood,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(28.dp),
                )
            }
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
            if (!brand.isNullOrBlank()) {
                Text(
                    text = brand,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

@Composable
private fun CalorieSummary(
    calories: Float,
    perLabel: String,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
    ) {
        Row(
            modifier = Modifier.padding(MaterialTheme.spacing.md),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.md),
        ) {
            Surface(
                modifier = Modifier.size(48.dp),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primary,
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Filled.LocalFireDepartment,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(26.dp),
                    )
                }
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Calories",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = formatMacroValue(calories),
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary,
                    )
                    Text(
                        text = " kcal",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(bottom = 4.dp),
                    )
                }
            }

            Text(
                text = "per $perLabel",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.End,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(0.8f),
            )
        }
    }
}

@Composable
private fun MacroRow(
    nutrition: NutritionInfo,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.sm),
    ) {
        MacroTile("Protein", formatMacroValue(nutrition.protein), ProteinColor, Modifier.weight(1f))
        MacroTile("Carbs", formatMacroValue(nutrition.carbs), CarbsColor, Modifier.weight(1f))
        MacroTile("Fat", formatMacroValue(nutrition.fat), FatColor, Modifier.weight(1f))
    }
}

@Composable
private fun MacroTile(
    label: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surface,
    ) {
        Column(
            modifier = Modifier.padding(vertical = MaterialTheme.spacing.md, horizontal = MaterialTheme.spacing.sm),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(width = 28.dp, height = 4.dp)
                    .background(color, CircleShape),
            )
            Text(
                text = "$value g",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(top = MaterialTheme.spacing.xs),
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun SecondaryNutrients(
    nutrition: NutritionInfo,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surface,
    ) {
        Column {
            NutrientLine("Fiber", "${formatMacroValue(nutrition.fiber)} g", FiberColor)
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = MaterialTheme.spacing.md),
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.15f),
            )
            NutrientLine("Sodium", "${formatMacroValue(nutrition.sodium)} mg", MaterialTheme.colorScheme.secondaryContainer)
        }
    }
}

@Composable
private fun NutrientLine(
    label: String,
    value: String,
    color: Color,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = MaterialTheme.spacing.md, vertical = MaterialTheme.spacing.sm + MaterialTheme.spacing.xs),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .background(color, CircleShape),
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .weight(1f)
                .padding(start = MaterialTheme.spacing.sm),
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}
