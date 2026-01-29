package com.example.responsiveapp.presentation.user_setup_screens.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.responsiveapp.presentation.ui.theme.spacing
import kotlin.math.abs
import kotlin.math.roundToInt

enum class WeightUnit {
    KG, LBS
}

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

@Composable
private fun UnitSelector(
    selectedUnit: WeightUnit,
    onUnitChanged: (WeightUnit) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                MaterialTheme.colorScheme.surfaceVariant,
                RoundedCornerShape(16.dp)
            )
            .padding(6.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        UnitTab(
            text = "KG",
            isSelected = selectedUnit == WeightUnit.KG,
            onClick = { onUnitChanged(WeightUnit.KG) },
            modifier = Modifier.weight(1f)
        )
        UnitTab(
            text = "LBS",
            isSelected = selectedUnit == WeightUnit.LBS,
            onClick = { onUnitChanged(WeightUnit.LBS) },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun UnitTab(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = if (isSelected) {
            MaterialTheme.colorScheme.primary
        } else {
            Color.Transparent
        }
    ) {
        Box(
            modifier = Modifier.padding(vertical = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    color = if (isSelected) {
                        MaterialTheme.colorScheme.onPrimary
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    }
                )
            )
        }
    }
}

@Composable
private fun WeightPicker(
    weight: Float?,
    onWeightChanged: (Float) -> Unit,
    minWeight: Float,
    maxWeight: Float,
    unit: String
) {
    // State for the current weight being displayed
    var displayWeight by remember(weight, unit) {
        mutableFloatStateOf(weight ?: if (unit == "kg") 70f else 154f)
    }

    val coroutineScope = rememberCoroutineScope()

    // Update display weight when prop changes
    LaunchedEffect(weight) {
        if (weight != null) {
            displayWeight = weight
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.weight(1f))

        // Large weight display
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(bottom = 32.dp)
        ) {
            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = String.format("%.1f", displayWeight),
                    style = MaterialTheme.typography.displayLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 80.sp,
                        letterSpacing = (-3).sp
                    )
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = unit,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                    ),
                    modifier = Modifier.padding(bottom = 12.dp)
                )
            }
        }

        // Ruler Scale
        RulerScale(
            currentWeight = displayWeight,
            minWeight = minWeight,
            maxWeight = maxWeight,
            onWeightChange = { newWeight ->
                displayWeight = newWeight
                onWeightChanged(newWeight)
            }
        )

        Spacer(modifier = Modifier.height(48.dp))

        // Increment/Decrement Buttons
        Row(
            modifier = Modifier.fillMaxWidth(0.6f),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Decrement Button
            FilledIconButton(
                onClick = {
                    val newWeight = ((displayWeight - 0.5f) * 10).roundToInt() / 10f
                    if (newWeight >= minWeight) {
                        displayWeight = newWeight
                        onWeightChanged(newWeight)
                    }
                },
                modifier = Modifier.size(56.dp),
                shape = CircleShape,
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Remove,
                    contentDescription = "Decrease",
                    modifier = Modifier.size(24.dp)
                )
            }

            // Increment Button
            FilledIconButton(
                onClick = {
                    val newWeight = ((displayWeight + 0.5f) * 10).roundToInt() / 10f
                    if (newWeight <= maxWeight) {
                        displayWeight = newWeight
                        onWeightChanged(newWeight)
                    }
                },
                modifier = Modifier.size(56.dp),
                shape = CircleShape,
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Increase",
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
private fun RulerScale(
    currentWeight: Float,
    minWeight: Float,
    maxWeight: Float,
    onWeightChange: (Float) -> Unit
) {
    val density = LocalDensity.current

    // Capture colors before Canvas
    val primaryColor = MaterialTheme.colorScheme.primary
    val onBackgroundColor = MaterialTheme.colorScheme.onBackground

    // Scale configuration
    val scaleWidth = 350.dp
    val scaleWidthPx = with(density) { scaleWidth.toPx() }
    val pixelsPerUnit = scaleWidthPx / 20f // Show ±10 units range

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .width(scaleWidth)
                .height(140.dp)
                .pointerInput(Unit) {
                    var totalDrag = 0f
                    detectHorizontalDragGestures(
                        onDragStart = {
                            totalDrag = 0f
                        },
                        onDragEnd = {
                            totalDrag = 0f
                        },
                        onDragCancel = {
                            totalDrag = 0f
                        }
                    ) { change, dragAmount ->
                        change.consume()
                        totalDrag += dragAmount

                        // Calculate weight change (negative because we drag opposite direction)
                        val weightChange = -(totalDrag / pixelsPerUnit)
                        val newWeight = currentWeight + weightChange

                        // Round to 0.1 and constrain
                        val roundedWeight = ((newWeight * 10).roundToInt() / 10f)
                            .coerceIn(minWeight, maxWeight)

                        if (roundedWeight != currentWeight) {
                            onWeightChange(roundedWeight)
                            totalDrag = 0f // Reset after successful change
                        }
                    }
                }
        ) {
            // Canvas for ruler marks
            Canvas(
                modifier = Modifier.fillMaxSize()
            ) {
                val centerX = size.width / 2
                val centerY = size.height / 2

                // Draw scale marks
                val range = 10f
                var weight = (currentWeight - range).coerceAtLeast(minWeight)
                val endWeight = (currentWeight + range).coerceAtMost(maxWeight)

                while (weight <= endWeight) {
                    val offsetFromCenter = (weight - currentWeight) * pixelsPerUnit
                    val x = centerX + offsetFromCenter

                    if (x >= 0f && x <= size.width) {
                        val isMajorMark = (weight * 10).roundToInt() % 10 == 0
                        val isHalfMark = (weight * 10).roundToInt() % 5 == 0

                        val lineHeight = when {
                            isMajorMark -> 65f
                            isHalfMark -> 45f
                            else -> 28f
                        }

                        val lineWidth = when {
                            isMajorMark -> 3.5f
                            isHalfMark -> 2.5f
                            else -> 2f
                        }

                        // Distance-based alpha with smoother gradient
                        val distance = abs(offsetFromCenter)
                        val distanceAlpha = when {
                            distance < 50 -> 1f
                            distance < 100 -> 0.7f
                            distance < 150 -> 0.4f
                            distance < 200 -> 0.2f
                            else -> 0.1f
                        }

                        // Highlight the centered mark
                        val isCenter = abs(weight - currentWeight) < 0.15f

                        val color = if (isCenter) {
                            primaryColor
                        } else {
                            onBackgroundColor.copy(alpha = distanceAlpha * 0.4f)
                        }

                        // Draw tick mark
                        drawLine(
                            color = color,
                            start = Offset(x, centerY - lineHeight / 2),
                            end = Offset(x, centerY + lineHeight / 2),
                            strokeWidth = if (isCenter) lineWidth * 1.3f else lineWidth,
                            cap = StrokeCap.Round
                        )
                    }

                    weight += 0.1f
                }
            }

            // Weight number labels
            val range = 10f
            var weight = (currentWeight - range).coerceAtLeast(minWeight)
            val endWeight = (currentWeight + range).coerceAtMost(maxWeight)

            while (weight <= endWeight) {
                val isMajorMark = (weight * 10).roundToInt() % 10 == 0

                if (isMajorMark) {
                    val offsetFromCenter = (weight - currentWeight) * pixelsPerUnit
                    val distance = abs(offsetFromCenter)

                    if (distance < 160f) {
                        val distanceAlpha = when {
                            distance < 50 -> 1f
                            distance < 100 -> 0.7f
                            distance < 150 -> 0.5f
                            else -> 0.3f
                        }

                        val xDp = with(density) { (scaleWidthPx / 2 + offsetFromCenter).toDp() }
                        val isCenter = abs(weight - currentWeight) < 0.6f

                        Box(
                            modifier = Modifier
                                .offset(x = xDp - 24.dp, y = 85.dp)
                                .width(48.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = weight.roundToInt().toString(),
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = if (isCenter) {
                                        MaterialTheme.colorScheme.primary
                                    } else {
                                        MaterialTheme.colorScheme.onBackground.copy(alpha = distanceAlpha)
                                    },
                                    fontWeight = if (isCenter) FontWeight.Bold else FontWeight.Medium,
                                    fontSize = if (isCenter) 15.sp else 13.sp
                                ),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.alpha(if (distance > 180) 0f else 1f)
                            )
                        }
                    }
                }

                weight += 0.1f
            }
        }

        // Center indicator line
        Box(
            modifier = Modifier
                .width(3.dp)
                .height(80.dp)
                .background(
                    color = primaryColor,
                    shape = RoundedCornerShape(2.dp)
                )
        )
    }
}