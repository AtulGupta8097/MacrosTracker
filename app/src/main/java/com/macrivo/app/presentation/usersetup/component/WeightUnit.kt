package com.macrivo.app.presentation.usersetup.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.macrivo.app.presentation.ui.theme.spacing
import kotlin.math.abs
import kotlinx.coroutines.flow.distinctUntilChanged
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

        // A new ruler (and scroll state) per unit, because kg and lbs have different ranges
        key(minWeight, maxWeight) {
            RulerScale(
                currentWeight = displayWeight,
                minWeight = minWeight,
                maxWeight = maxWeight,
                onWeightChange = { newWeight ->
                    displayWeight = newWeight
                    onWeightChanged(newWeight)
                }
            )
        }

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
    val minTenths = (minWeight * 10).roundToInt()
    val maxTenths = (maxWeight * 10).roundToInt()
    val count = maxTenths - minTenths + 1
    fun indexOf(weight: Float) = ((weight * 10).roundToInt() - minTenths).coerceIn(0, count - 1)

    val itemWidth = 8.dp
    val listState = rememberLazyListState(initialFirstVisibleItemIndex = indexOf(currentWeight))
    val flingBehavior = rememberSnapFlingBehavior(listState)
    val haptic = LocalHapticFeedback.current

    val latestWeight = rememberUpdatedState(currentWeight)
    val latestOnChange = rememberUpdatedState(onWeightChange)
    var isProgrammaticScroll by remember { mutableStateOf(false) }

    // the tick that is closest to the middle of the ruler
    val centerIndex by remember {
        derivedStateOf {
            val info = listState.layoutInfo
            val middle = (info.viewportStartOffset + info.viewportEndOffset) / 2
            info.visibleItemsInfo
                .minByOrNull { abs(it.offset + it.size / 2 - middle) }
                ?.index
                ?: indexOf(latestWeight.value)
        }
    }

    // ruler -> value
    LaunchedEffect(listState) {
        snapshotFlow { centerIndex }
            .distinctUntilChanged()
            .collect { index ->
                if (isProgrammaticScroll) return@collect
                val tenths = minTenths + index
                if ((latestWeight.value * 10).roundToInt() != tenths) {
                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    latestOnChange.value(tenths / 10f)
                }
            }
    }

    // value -> ruler (the +/- buttons, or a unit switch)
    LaunchedEffect(currentWeight) {
        val target = indexOf(currentWeight)
        if (!listState.isScrollInProgress && target != centerIndex) {
            isProgrammaticScroll = true
            try {
                listState.animateScrollToItem(target)
            } finally {
                isProgrammaticScroll = false
            }
        }
    }

    val primary = MaterialTheme.colorScheme.primary
    val tickColor = MaterialTheme.colorScheme.onSurfaceVariant
    val surfaceColor = MaterialTheme.colorScheme.surface

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .height(RULER_HEIGHT)
    ) {
        val sidePadding = (maxWidth - itemWidth) / 2

        LazyRow(
            state = listState,
            flingBehavior = flingBehavior,
            contentPadding = PaddingValues(horizontal = sidePadding),
            modifier = Modifier.fillMaxSize()
        ) {
            items(count = count, key = { it }) { index ->
                val tenths = minTenths + index
                val isWhole = tenths % 10 == 0
                val isHalf = tenths % 5 == 0
                val isCenter = index == centerIndex

                Box(
                    modifier = Modifier
                        .width(itemWidth)
                        .fillMaxHeight(),
                    contentAlignment = Alignment.TopCenter
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val baseY = RULER_BASELINE.toPx()
                        val tickHeight = when {
                            isWhole -> 44.dp
                            isHalf -> 30.dp
                            else -> 18.dp
                        }.toPx()
                        val tickWidth = when {
                            isWhole -> 2.5.dp
                            isHalf -> 2.dp
                            else -> 1.5.dp
                        }.toPx()
                        val alpha = when {
                            isWhole -> 0.9f
                            isHalf -> 0.6f
                            else -> 0.35f
                        }

                        // baseline, continuous across ticks
                        drawLine(
                            color = tickColor.copy(alpha = 0.35f),
                            start = Offset(0f, baseY),
                            end = Offset(size.width, baseY),
                            strokeWidth = 1.dp.toPx()
                        )
                        drawLine(
                            color = tickColor.copy(alpha = alpha),
                            start = Offset(size.width / 2, baseY - tickHeight),
                            end = Offset(size.width / 2, baseY),
                            strokeWidth = tickWidth,
                            cap = StrokeCap.Round
                        )
                    }

                    if (isWhole) {
                        Text(
                            text = (tenths / 10).toString(),
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = if (isCenter) FontWeight.Bold else FontWeight.Medium,
                                color = if (isCenter) primary else tickColor
                            ),
                            maxLines = 1,
                            softWrap = false,
                            modifier = Modifier
                                .padding(top = RULER_BASELINE + 8.dp)
                                .wrapContentWidth(unbounded = true)
                        )
                    }
                }
            }
        }

        // soft fade at both ends
        Box(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .width(48.dp)
                .fillMaxHeight()
                .background(Brush.horizontalGradient(listOf(surfaceColor, surfaceColor.copy(alpha = 0f))))
        )
        Box(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .width(48.dp)
                .fillMaxHeight()
                .background(Brush.horizontalGradient(listOf(surfaceColor.copy(alpha = 0f), surfaceColor)))
        )

        // fixed needle with a pointer on top
        Canvas(modifier = Modifier.fillMaxSize()) {
            val x = size.width / 2
            val baseY = RULER_BASELINE.toPx()
            val pointer = Path().apply {
                moveTo(x - 7.dp.toPx(), 0f)
                lineTo(x + 7.dp.toPx(), 0f)
                lineTo(x, 10.dp.toPx())
                close()
            }
            drawPath(pointer, primary)
            drawLine(
                color = primary,
                start = Offset(x, 10.dp.toPx()),
                end = Offset(x, baseY),
                strokeWidth = 3.dp.toPx(),
                cap = StrokeCap.Round
            )
        }
    }
}

private val RULER_HEIGHT = 104.dp
private val RULER_BASELINE = 66.dp
