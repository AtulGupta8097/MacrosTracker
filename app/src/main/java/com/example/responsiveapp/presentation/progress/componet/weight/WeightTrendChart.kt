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

private val ChartHeight = 200.dp

private const val Y_AXIS_STEPS = 4
private const val EnterAnimationDurationMs = 500

@Composable
fun WeightTrendChart(
    points: List<WeightChartPointUi>,
    modifier: Modifier = Modifier,
) {
    if (points.isEmpty()) return

    val surfaceColor = MaterialTheme.colorScheme.surface
    val accentColor = MaterialTheme.colorScheme.primary
    val onAccentColor = MaterialTheme.colorScheme.onPrimary

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
                            fullHeight / 3
                        },
                    ),
    ) {
        BoxWithConstraints(
            modifier = modifier
                .fillMaxWidth()
                .height(ChartHeight)
                .padding(start = 4.dp),
        ) {
            val availableWidth = maxWidth

            val stepWidth = remember(
                availableWidth,
                points.size,
            ) {
                availableWidth /
                        points.size.coerceAtLeast(1)
            }

            val dotRadius = remember(points.size) {
                when {
                    points.size <= 7 -> 4.dp
                    points.size <= 15 -> 3.dp
                    else -> 2.dp
                }
            }

            val labelStep = remember(points.size) {
                when {
                    points.size <= 8 -> 1
                    points.size <= 20 -> 3
                    else -> 5
                }
            }

            val axisRange = remember(points) {
                val lowest = points.minOf {
                    it.weight
                }

                val highest = points.maxOf {
                    it.weight
                }

                val range = highest - lowest

                val headroom = (range * 0.2f)
                    .coerceAtLeast(1f)

                (lowest - headroom) to
                        (highest + headroom)
            }

            val weightPoints = remember(points) {
                points.mapIndexed { index, point ->
                    Point(
                        index.toFloat(),
                        point.weight,
                    )
                }
            }

            val xAxisData = remember(
                points,
                stepWidth,
                labelStep,
            ) {
                AxisData.Builder()
                    .axisStepSize(stepWidth)
                    .steps(points.size - 1)
                    .indicatorLineWidth(0.dp)
                    .labelData { index ->
                        index.toString()
                    }
                    .labelAndAxisLinePadding(14.dp)
                    .build()
            }

            val yAxisData = remember(axisRange) {
                val (min, max) = axisRange

                AxisData.Builder()
                    .steps(Y_AXIS_STEPS)
                    .indicatorLineWidth(0.dp)
                    .labelAndAxisLinePadding(20.dp)
                    .axisOffset(24.dp)
                    .labelData { index ->
                        (min + index * ((max - min) / Y_AXIS_STEPS))
                            .roundToInt()
                            .toString()
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
                                        alpha = 0.22f,
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

                                            "${points[index].weight} kg"
                                        },
                                    ),
                            ),
                        ),
                    ),

                    xAxisData = xAxisData,
                    yAxisData = yAxisData,

                    gridLines = null,

                    backgroundColor = surfaceColor,
                )
            }

            LineChart(
                modifier = Modifier.fillMaxSize(),
                lineChartData = lineChartData,
            )
        }
    }
}

@Preview
@Composable
private fun PrevWeightTrendChart() {
    ResponsiveAppTheme {
        WeightTrendChart(
            points = listOf(
                WeightChartPointUi(
                    "Jul 18",
                    60f,
                ),
                WeightChartPointUi(
                    "Jul 25",
                    64f,
                ),
                WeightChartPointUi(
                    "Aug 1",
                    70f,
                ),
                WeightChartPointUi(
                    "Aug 10",
                    68f,
                ),
                WeightChartPointUi(
                    "Aug 18",
                    65f,
                ),
            ),
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
