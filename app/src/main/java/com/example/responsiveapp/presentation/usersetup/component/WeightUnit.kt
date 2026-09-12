package com.example.responsiveapp.presentation.usersetup.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
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
fun UnitSelector(
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
fun WeightPicker(
    weight: Float?,
    onWeightChanged: (Float) -> Unit,
    minWeight: Float,
    maxWeight: Float,
    unit: String
) {
    var displayWeight by remember(weight, unit) {
        mutableFloatStateOf(weight ?: if (unit == "kg") 70f else 154f)
    }

    LaunchedEffect(weight) {
        if (weight != null) {
            displayWeight = weight
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .background(
                MaterialTheme.colorScheme.surface,
                RoundedCornerShape(24.dp)
            )
            .padding(vertical = MaterialTheme.spacing.lg)
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
                    fontSize = 72.sp,
                    letterSpacing = (-3).sp
                )
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = unit,
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                modifier = Modifier.padding(bottom = 10.dp)
            )
        }

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.lg))

        RulerScale(
            currentWeight = displayWeight,
            minWeight = minWeight,
            maxWeight = maxWeight,
            onWeightChange = { newWeight ->
                displayWeight = newWeight
                onWeightChanged(newWeight)
            }
        )

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.lg))

        Row(
            modifier = Modifier.fillMaxWidth(0.55f),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            FilledIconButton(
                onClick = {
                    val newWeight = ((displayWeight - 0.5f) * 10).roundToInt() / 10f
                    if (newWeight >= minWeight) {
                        displayWeight = newWeight
                        onWeightChanged(newWeight)
                    }
                },
                modifier = Modifier.size(52.dp),
                shape = CircleShape,
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Remove,
                    contentDescription = "Decrease",
                    modifier = Modifier.size(22.dp)
                )
            }

            FilledIconButton(
                onClick = {
                    val newWeight = ((displayWeight + 0.5f) * 10).roundToInt() / 10f
                    if (newWeight <= maxWeight) {
                        displayWeight = newWeight
                        onWeightChanged(newWeight)
                    }
                },
                modifier = Modifier.size(52.dp),
                shape = CircleShape,
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Increase",
                    modifier = Modifier.size(22.dp)
                )
            }
        }
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
    val primaryColor = MaterialTheme.colorScheme.primary
    val onBackgroundColor = MaterialTheme.colorScheme.onBackground
    val surfaceColor = MaterialTheme.colorScheme.surface

    val scaleWidth = 320.dp
    val scaleWidthPx = with(density) { scaleWidth.toPx() }
    val pixelsPerUnit = scaleWidthPx / 20f

    val latestWeight = rememberUpdatedState(currentWeight)
    val latestMin = rememberUpdatedState(minWeight)
    val latestMax = rememberUpdatedState(maxWeight)
    val latestOnChange = rememberUpdatedState(onWeightChange)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(130.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .width(scaleWidth)
                .height(130.dp)
                .pointerInput(Unit) {
                    var totalDrag = 0f
                    detectHorizontalDragGestures(
                        onDragStart = { totalDrag = 0f },
                        onDragEnd = { totalDrag = 0f },
                        onDragCancel = { totalDrag = 0f }
                    ) { change, dragAmount ->
                        change.consume()
                        totalDrag += dragAmount

                        val weightChange = -(totalDrag / pixelsPerUnit)
                        val newWeight = latestWeight.value + weightChange

                        val roundedWeight = ((newWeight * 10).roundToInt() / 10f)
                            .coerceIn(latestMin.value, latestMax.value)

                        if (roundedWeight != latestWeight.value) {
                            latestOnChange.value(roundedWeight)
                            totalDrag = 0f
                        }
                    }
                }
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val centerX = size.width / 2
                val centerY = size.height / 2

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
                            isMajorMark -> 60f
                            isHalfMark -> 42f
                            else -> 26f
                        }
                        val lineWidth = when {
                            isMajorMark -> 3.5f
                            isHalfMark -> 2.5f
                            else -> 2f
                        }

                        val distance = abs(offsetFromCenter)
                        val distanceAlpha = when {
                            distance < 50 -> 1f
                            distance < 100 -> 0.7f
                            distance < 150 -> 0.4f
                            else -> 0.15f
                        }

                        val isCenter = abs(weight - currentWeight) < 0.15f
                        val color = if (isCenter) {
                            primaryColor
                        } else {
                            onBackgroundColor.copy(alpha = distanceAlpha * 0.4f)
                        }

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

            run {
                val range = 10f
                var weight = (currentWeight - range).coerceAtLeast(minWeight)
                val endWeight = (currentWeight + range).coerceAtMost(maxWeight)

                while (weight <= endWeight) {
                    val isMajorMark = (weight * 10).roundToInt() % 10 == 0
                    if (isMajorMark) {
                        val offsetFromCenter = (weight - currentWeight) * pixelsPerUnit
                        val distance = abs(offsetFromCenter)

                        if (distance < 150f) {
                            val distanceAlpha = when {
                                distance < 50 -> 1f
                                distance < 100 -> 0.7f
                                else -> 0.4f
                            }
                            val xDp = with(density) { (scaleWidthPx / 2 + offsetFromCenter).toDp() }
                            val isCenter = abs(weight - currentWeight) < 0.6f

                            Box(
                                modifier = Modifier
                                    .offset(x = xDp - 24.dp, y = 78.dp)
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
                                    modifier = Modifier.alpha(if (distance > 160) 0f else 1f)
                                )
                            }
                        }
                    }
                    weight += 0.1f
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterStart)
                    .width(48.dp)
                    .height(130.dp)
                    .background(
                        Brush.horizontalGradient(
                            listOf(surfaceColor, surfaceColor.copy(alpha = 0f))
                        )
                    )
            )
            Box(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .width(48.dp)
                    .height(130.dp)
                    .background(
                        Brush.horizontalGradient(
                            listOf(surfaceColor.copy(alpha = 0f), surfaceColor)
                        )
                    )
            )
        }

        Box(
            modifier = Modifier
                .width(3.dp)
                .height(74.dp)
                .background(
                    color = primaryColor,
                    shape = RoundedCornerShape(2.dp)
                )
        )
    }
}