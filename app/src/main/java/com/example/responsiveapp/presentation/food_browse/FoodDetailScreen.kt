package com.example.responsiveapp.presentation.food_browse

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.responsiveapp.domain.model.FoodDetail
import com.example.responsiveapp.domain.model.NutritionInfo
import com.example.responsiveapp.domain.model.Serving
import com.example.responsiveapp.domain.model.scale
import com.example.responsiveapp.presentation.ui.theme.spacing
import kotlin.math.roundToInt

@Composable
fun FoodDetailScreen(
    foodDetail: FoodDetail?,
    isLoading: Boolean,
    error: String?,
    onBack: () -> Unit
) {
    var selectedServing by remember(foodDetail) {
        mutableStateOf(foodDetail?.defaultServing)
    }
    var quantity by remember { mutableFloatStateOf(1f) }

    // Recompute scaled nutrition whenever serving or quantity changes
    val scaledNutrition = remember(selectedServing, quantity) {
        selectedServing?.nutrition?.scale(quantity)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = MaterialTheme.spacing.sm),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }
            Text(
                text = foodDetail?.name ?: if (isLoading) "Loading..." else "Food Detail",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.weight(1f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Box(modifier = Modifier.fillMaxSize()) {
            when {
                isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                error != null -> {
                    Column(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(MaterialTheme.spacing.lg),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.sm)
                    ) {
                        Text(
                            text = "Something went wrong",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = error,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }
                }

                foodDetail != null && selectedServing != null && scaledNutrition != null -> {
                    FoodDetailContent(
                        foodDetail = foodDetail,
                        selectedServing = selectedServing!!,
                        quantity = quantity,
                        scaledNutrition = scaledNutrition,
                        onServingSelected = {
                            selectedServing = it
                            quantity = 1f  // reset quantity when switching serving
                        },
                        onQuantityChanged = { quantity = it },
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}

@Composable
private fun FoodDetailContent(
    foodDetail: FoodDetail,
    selectedServing: Serving,
    quantity: Float,
    scaledNutrition: NutritionInfo,
    onServingSelected: (Serving) -> Unit,
    onQuantityChanged: (Float) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .navigationBarsPadding(),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.md)
    ) {
        // Brand
        foodDetail.brand?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                modifier = Modifier.padding(horizontal = MaterialTheme.spacing.md)
            )
        }

        // ── Calorie callout card ─────────────────────────────────────────
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = MaterialTheme.spacing.md),
            color = MaterialTheme.colorScheme.primaryContainer,
            shape = MaterialTheme.shapes.large
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(MaterialTheme.spacing.lg),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                AnimatedContent(
                    targetState = scaledNutrition.calories.roundToInt(),
                    transitionSpec = { fadeIn() togetherWith fadeOut() },
                    label = "CaloriesAnim"
                ) { cal ->
                    Text(
                        text = "$cal",
                        style = MaterialTheme.typography.displaySmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    )
                }
                Text(
                    text = "calories · ${selectedServing.description}",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                )
            }
        }

        // ── Macros row ───────────────────────────────────────────────────
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = MaterialTheme.spacing.md),
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.sm)
        ) {
            MacroCard("Protein", scaledNutrition.protein, "g", Modifier.weight(1f))
            MacroCard("Carbs",   scaledNutrition.carbs,   "g", Modifier.weight(1f))
            MacroCard("Fat",     scaledNutrition.fat,     "g", Modifier.weight(1f))
        }

        // ── Serving selector (only if multiple servings) ─────────────────
        if (foodDetail.servings.size > 1) {
            ServingSelector(
                servings = foodDetail.servings,
                selectedServing = selectedServing,
                onServingSelected = onServingSelected,
            )
        }

        QuantityControl(
            quantity = quantity,
            onQuantityChanged = onQuantityChanged,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = MaterialTheme.spacing.md)
        )

        // ── Nutrition table ──────────────────────────────────────────────
        NutritionTable(
            nutrition = scaledNutrition,
            modifier = Modifier.padding(horizontal = MaterialTheme.spacing.md)
        )

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.md))
    }
}

@Composable
private fun ServingSelector(
    servings: List<Serving>,
    selectedServing: Serving,
    onServingSelected: (Serving) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.xs)) {
        Text(
            text = "Serving size",
            style = MaterialTheme.typography.labelMedium.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant
            ),
            modifier = Modifier.padding(horizontal = MaterialTheme.spacing.md)
        )
        LazyRow(
            contentPadding = PaddingValues(horizontal = MaterialTheme.spacing.md),
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.sm)
        ) {
            items(servings, key = { it.id }) { serving ->
                val isSelected = serving.id == selectedServing.id
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(
                            if (isSelected) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.surfaceVariant
                        )
                        .border(
                            width = if (isSelected) 0.dp else 1.dp,
                            color = MaterialTheme.colorScheme.outline,
                            shape = RoundedCornerShape(50)
                        )
                        .clickable { onServingSelected(serving) }
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = serving.description,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = if (isSelected) MaterialTheme.colorScheme.onPrimary
                            else MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                        )
                    )
                }
            }
        }
    }
}


@Composable
private fun QuantityControl(
    quantity: Float,
    onQuantityChanged: (Float) -> Unit,
    modifier: Modifier = Modifier,
) {
    var inputText by remember(quantity) {
        mutableStateOf(
            if (quantity == quantity.roundToInt().toFloat()) quantity.roundToInt().toString()
            else "%.1f".format(quantity)
        )
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.xs)
    ) {
        Text(
            text = "Quantity",
            style = MaterialTheme.typography.labelMedium.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.sm)
        ) {
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier
                    .size(40.dp)
                    .clickable {
                        val newQty = (quantity - 0.5f).coerceAtLeast(0.5f)
                        onQuantityChanged(newQty)
                    }
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Remove,
                        contentDescription = "Decrease",
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            OutlinedTextField(
                value = inputText,
                onValueChange = { text ->
                    inputText = text
                    text.toFloatOrNull()?.let { parsed ->
                        if (parsed > 0f) onQuantityChanged(parsed)
                    }
                },
                modifier = Modifier.width(100.dp),
                textStyle = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true,
                shape = MaterialTheme.shapes.medium,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                )
            )

            // Increment
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier
                    .size(40.dp)
                    .clickable { onQuantityChanged(quantity + 0.5f) }
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Increase",
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun MacroCard(
    label: String,
    value: Float,
    unit: String,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier.padding(MaterialTheme.spacing.sm),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            AnimatedContent(
                targetState = value.roundToInt(),
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "${label}Anim"
            ) { v ->
                Text(
                    text = "$v$unit",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
    }
}

@Composable
private fun NutritionTable(
    nutrition: NutritionInfo,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        shape = MaterialTheme.shapes.medium,
        tonalElevation = 1.dp
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            NutritionRow("Total Fat",         "${nutrition.fat.roundToInt()}g",                isHeader = true)
            NutritionRow("Saturated Fat",     "${nutrition.saturatedFat.roundToInt()}g",       indent = true)
            NutritionRow("Trans Fat",         "${nutrition.transFat.roundToInt()}g",           indent = true)
            NutritionRow("Polyunsaturated",   "${nutrition.polyunsaturatedFat.roundToInt()}g", indent = true)
            NutritionRow("Monounsaturated",   "${nutrition.monounsaturatedFat.roundToInt()}g", indent = true)
            HorizontalDivider()
            NutritionRow("Cholesterol",       "${nutrition.cholesterol.roundToInt()}mg",       isHeader = true)
            NutritionRow("Sodium",            "${nutrition.sodium.roundToInt()}mg",            isHeader = true)
            HorizontalDivider()
            NutritionRow("Total Carbs",       "${nutrition.carbs.roundToInt()}g",              isHeader = true)
            NutritionRow("Dietary Fiber",     "${nutrition.fiber.roundToInt()}g",              indent = true)
            NutritionRow("Total Sugars",      "${nutrition.sugar.roundToInt()}g",              indent = true)
            NutritionRow("Added Sugars",      "${nutrition.addedSugars.roundToInt()}g",        indent = true)
            HorizontalDivider()
            NutritionRow("Protein",           "${nutrition.protein.roundToInt()}g",            isHeader = true)
            HorizontalDivider()
            NutritionRow("Potassium",         "${nutrition.potassium.roundToInt()}mg")
            NutritionRow("Vitamin A",         "${nutrition.vitaminA.roundToInt()}mcg")
            NutritionRow("Vitamin C",         "${nutrition.vitaminC.roundToInt()}mg")
            NutritionRow("Vitamin D",         "${nutrition.vitaminD.roundToInt()}mcg")
            NutritionRow("Calcium",           "${nutrition.calcium.roundToInt()}mg")
            NutritionRow("Iron",              "${nutrition.iron.roundToInt()}mg")
        }
    }
}

@Composable
private fun NutritionRow(
    label: String,
    value: String,
    isHeader: Boolean = false,
    indent: Boolean = false,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = if (indent) 32.dp else MaterialTheme.spacing.md,
                end = MaterialTheme.spacing.md,
                top = MaterialTheme.spacing.sm,
                bottom = MaterialTheme.spacing.sm,
            ),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = if (isHeader)
                MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
            else
                MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
        )
        AnimatedContent(
            targetState = value,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            label = "${label}ValueAnim"
        ) { v ->
            Text(text = v, style = MaterialTheme.typography.bodyMedium)
        }
    }
}