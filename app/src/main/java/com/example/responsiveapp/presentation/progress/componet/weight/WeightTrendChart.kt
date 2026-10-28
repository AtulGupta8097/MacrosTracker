package com.example.responsiveapp.presentation.progress.componet.weight

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import co.yml.charts.axis.AxisData
import co.yml.charts.common.model.Point
import co.yml.charts.ui.linechart.LineChart
import co.yml.charts.ui.linechart.model.IntersectionPoint
import co.yml.charts.ui.linechart.model.Line
import co.yml.charts.ui.linechart.model.LineChartData
import co.yml.charts.ui.linechart.model.LinePlotData
import co.yml.charts.ui.linechart.model.LineStyle
import co.yml.charts.ui.linechart.model.LineType
import co.yml.charts.ui.linechart.model.SelectionHighlightPoint
import co.yml.charts.ui.linechart.model.SelectionHighlightPopUp
import co.yml.charts.ui.linechart.model.ShadowUnderLine
import com.example.responsiveapp.presentation.progress.model.WeightChartPointUi
import com.example.responsiveapp.presentation.ui.theme.ResponsiveAppTheme
import kotlin.math.roundToInt

private val ChartHeight = 205.dp
private const val Y_AXIS_STEPS = 4
private const val EnterAnimationDurationMs = 400
private val Y_AXIS_WIDTH = 36.dp
private val CHART_END_PADDING = 16.dp
private const val MIN_VISIBLE_WEIGHT_RANGE = 8f
private val SingleEntryCardShape = RoundedCornerShape(24.dp)

@Composable
fun WeightTrendChart(
    points: List<WeightChartPointUi>,
    modifier: Modifier = Modifier,
) {
    if (points.isEmpty()) return

    if (points.size == 1) {
        SingleWeightEntry(point = points[0], modifier = modifier)
        return
    }

    val colorScheme = MaterialTheme.colorScheme

    val surfaceColor = colorScheme.surface
    val accentColor = colorScheme.primary
    val onAccentColor = colorScheme.onPrimary
    val axisLabelColor = colorScheme.onSurfaceVariant

    var isVisible by remember(points) {
        mutableStateOf(false)
    }

    LaunchedEffect(points) {
        isVisible = true
    }

    AnimatedVisibility(
        visible = isVisible,
        enter =
            fadeIn(
                animationSpec = tween(
                    EnterAnimationDurationMs,
                ),
            ) +
                    slideInVertically(
                        animationSpec = tween(
                            EnterAnimationDurationMs,
                            easing = FastOutSlowInEasing,
                        ),
                        initialOffsetY = { fullHeight ->
                            fullHeight / 5
                        },
                    ),
    ) {
        BoxWithConstraints(
            modifier = modifier
                .fillMaxWidth()
                .height(ChartHeight)
                .padding(
                    start = 4.dp,
                    end = 4.dp,
                ),
        ) {
            val pointCount = points.size

            /*
             * Single entry gets a centered position.
             */
            val axisSteps = if (pointCount == 1) {
                2
            } else {
                pointCount - 1
            }

            val availableWidth =
                (maxWidth - Y_AXIS_WIDTH - CHART_END_PADDING)
                    .coerceAtLeast(1.dp)

            val stepWidth = remember(
                availableWidth,
                axisSteps,
            ) {
                availableWidth / axisSteps
            }

            val dotRadius = remember(pointCount) {
                when {
                    pointCount <= 7 -> 4.dp
                    pointCount <= 15 -> 3.dp
                    else -> 2.dp
                }
            }

            val axisRange = remember(points) {
                val lowest = points.minOf { it.weight }
                val highest = points.maxOf { it.weight }

                val actualRange = highest - lowest

                val visibleRange = maxOf(
                    actualRange * 1.5f,
                    MIN_VISIBLE_WEIGHT_RANGE,
                )

                val center = (lowest + highest) / 2f

                val min = center - visibleRange / 2f
                val max = center + visibleRange / 2f

                min to max
            }

            val weightPoints = remember(points) {
                if (pointCount == 1) {
                    listOf(
                        Point(
                            x = 1f,
                            y = points[0].weight,
                        ),
                    )
                } else {
                    points.mapIndexed { index, point ->
                        Point(
                            x = index.toFloat(),
                            y = point.weight,
                        )
                    }
                }
            }

            val xAxisData = remember(
                pointCount,
                stepWidth,
                axisSteps,
                axisLabelColor,
            ) {
                AxisData.Builder()
                    .axisStepSize(stepWidth)
                    .steps(axisSteps)
                    .indicatorLineWidth(0.dp)
                    .bottomPadding(20.dp)
                    .startDrawPadding(12.dp)
                    .labelAndAxisLinePadding(8.dp)
                    .axisLabelColor(axisLabelColor)
                    .axisLineColor(
                        axisLabelColor.copy(alpha = 0.35f),
                    )
                    .labelData { index ->

                        if (pointCount == 1) {
                            if (index == 1) {
                                "1"
                            } else {
                                ""
                            }
                        } else if (index in 0 until pointCount) {
                            (index + 1).toString()
                        } else {
                            ""
                        }
                    }
                    .build()
            }

            /*
             * ---------------------------------------------------------
             * Y AXIS
             * ---------------------------------------------------------
             */
            val yAxisData = remember(
                axisRange,
                axisLabelColor,
            ) {
                val (min, max) = axisRange

                AxisData.Builder()
                    .steps(Y_AXIS_STEPS)
                    .indicatorLineWidth(0.dp)
                    .axisOffset(Y_AXIS_WIDTH)
                    .labelAndAxisLinePadding(8.dp)
                    .axisLabelColor(axisLabelColor)
                    .axisLineColor(
                        axisLabelColor.copy(alpha = 0.35f),
                    )
                    .labelData { index ->

                        val value =
                            min +
                                    index *
                                    ((max - min) / Y_AXIS_STEPS)

                        formatWeightAxisLabel(value)
                    }
                    .build()
            }

            val lineChartData = remember(
                weightPoints,
                xAxisData,
                yAxisData,
                dotRadius,
                accentColor,
                onAccentColor,
                surfaceColor,
            ) {
                LineChartData(
                    linePlotData = LinePlotData(
                        lines = listOf(
                            Line(
                                dataPoints = weightPoints,

                                lineStyle = LineStyle(
                                    color = accentColor,
                                    lineType = LineType.SmoothCurve(),
                                ),

                                intersectionPoint =
                                    IntersectionPoint(
                                        color = accentColor,
                                        radius = dotRadius,
                                    ),

                                selectionHighlightPoint =
                                    SelectionHighlightPoint(
                                        color = accentColor,
                                    ),

                                shadowUnderLine =
                                    ShadowUnderLine(
                                        color = accentColor,
                                        alpha = 0.08f,
                                    ),

                                selectionHighlightPopUp =
                                    SelectionHighlightPopUp(
                                        backgroundColor = accentColor,
                                        labelColor = onAccentColor,

                                        popUpLabel = { x, _ ->
                                            val index =
                                                x.roundToInt()
                                                    .coerceIn(
                                                        0,
                                                        points.lastIndex,
                                                    )

                                            "${formatWeight(points[index].weight)} kg"
                                        },
                                    ),
                            ),
                        ),
                    ),

                    xAxisData = xAxisData,
                    yAxisData = yAxisData,

                    gridLines = null,

                    backgroundColor = surfaceColor,

                    bottomPadding = 8.dp,
                )
            }

            LineChart(
                modifier = Modifier.fillMaxSize(),
                lineChartData = lineChartData,
            )
        }
    }
}

@Composable
private fun SingleWeightEntry(
    point: WeightChartPointUi,
    modifier: Modifier = Modifier,
) {
    val colorScheme = MaterialTheme.colorScheme

    Card(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = ChartHeight)
            .semantics(mergeDescendants = true) { },
        shape = SingleEntryCardShape,
        colors = CardDefaults.cardColors(
            containerColor = colorScheme.surfaceContainerLow,
        ),
        border = BorderStroke(
            width = 1.dp,
            color = colorScheme.outlineVariant.copy(alpha = 0.55f),
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = "YOUR FIRST WEIGH-IN",
                    style = MaterialTheme.typography.labelMedium,
                    color = colorScheme.primary,
                )

                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = formatWeight(point.weight),
                        style = MaterialTheme.typography.displaySmall.copy(
                            fontWeight = FontWeight.Bold,
                        ),
                        color = colorScheme.onSurface,
                        maxLines = 1,
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "kg",
                        modifier = Modifier.padding(bottom = 7.dp),
                        style = MaterialTheme.typography.titleMedium,
                        color = colorScheme.onSurfaceVariant,
                    )
                }

                Text(
                    text = point.label,
                    style = MaterialTheme.typography.bodyMedium,
                    color = colorScheme.onSurfaceVariant,
                    maxLines = 1,
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Great start!",
                    style = MaterialTheme.typography.titleSmall,
                    color = colorScheme.onSurface,
                )
                Text(
                    text = "Add another weigh-in to see your trend take shape.",
                    style = MaterialTheme.typography.bodySmall,
                    color = colorScheme.onSurfaceVariant,
                )
                TrendPreviewHint()
            }
        }
    }
}

@Composable
private fun TrendPreviewHint() {
    val colorScheme = MaterialTheme.colorScheme

    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
        if (maxWidth < 340.dp) {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                TrendPreviewLine()
                Text(
                    text = "A preview of your weight trend",
                    style = MaterialTheme.typography.labelSmall,
                    color = colorScheme.onSurfaceVariant,
                )
            }
        } else {
            Row(verticalAlignment = Alignment.CenterVertically) {
                TrendPreviewLine()
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "A preview of your weight trend",
                    style = MaterialTheme.typography.labelSmall,
                    color = colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@Composable
private fun TrendPreviewLine() {
    val colorScheme = MaterialTheme.colorScheme
    val accent = colorScheme.primary
    val outline = colorScheme.outlineVariant
    val strokeWidth = 2.dp
    val dotRadius = 3.dp

    Canvas(modifier = Modifier.size(width = 48.dp, height = 24.dp)) {
        val lineY = size.height / 2f
        val startX = dotRadius.toPx()
        val endX = size.width - dotRadius.toPx()
        val color = accent.copy(alpha = 0.42f)

        drawLine(
            color = color,
            start = Offset(startX, lineY),
            end = Offset(endX, lineY),
            strokeWidth = strokeWidth.toPx(),
            pathEffect = PathEffect.dashPathEffect(
                floatArrayOf(3.dp.toPx(), 3.dp.toPx()),
            ),
        )
        drawCircle(
            color = accent,
            radius = dotRadius.toPx(),
            center = Offset(startX, lineY),
        )
        drawCircle(
            color = outline,
            radius = dotRadius.toPx(),
            center = Offset(size.width / 2f, lineY),
            style = Stroke(width = strokeWidth.toPx()),
        )
        drawCircle(
            color = outline,
            radius = dotRadius.toPx(),
            center = Offset(endX, lineY),
            style = Stroke(width = strokeWidth.toPx()),
        )
    }
}

private fun formatWeight(weight: Float): String {
    return if (weight % 1f == 0f) {
        weight.toInt().toString()
    } else {
        "%.1f".format(weight)
    }
}

private fun formatWeightAxisLabel(
    weight: Float,
): String {
    return if (weight % 1f == 0f) {
        weight.roundToInt().toString()
    } else {
        "%.1f".format(weight)
    }
}


@Preview(showBackground = true)
@Composable
private fun PrevWeightTrendChartSinglePoint() {
    ResponsiveAppTheme {
        WeightTrendChart(
            points = listOf(
                WeightChartPointUi(
                    "Aug 25",
                    68.5f,
                ),
                WeightChartPointUi(
                    "Aug 26",
                    68.2f,
                ),
                WeightChartPointUi(
                    "Aug 27",
                    67.8f,
                ),
                WeightChartPointUi(
                    "Aug 28",
                    67.3f,
                ),
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        )
    }
}
