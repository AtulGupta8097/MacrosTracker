package com.example.responsiveapp.presentation.progress.componet.weight

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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

@Composable
fun WeightTrendChart(
    points: List<WeightChartPointUi>,
    modifier: Modifier = Modifier,
) {
    if (points.isEmpty()) return

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

private fun formatWeight(weight: Float, ): String {
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
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        )
    }
}