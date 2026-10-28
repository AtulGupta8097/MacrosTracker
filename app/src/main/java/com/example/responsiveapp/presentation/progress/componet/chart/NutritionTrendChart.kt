package com.example.responsiveapp.presentation.progress.componet.chart

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
import com.example.responsiveapp.presentation.progress.model.MacroChartPointUi
import com.example.responsiveapp.presentation.progress.model.ProgressRange
import com.example.responsiveapp.presentation.ui.theme.ResponsiveAppTheme
import kotlin.math.atan2
import kotlin.math.roundToInt

private val ChartHeight = 220.dp
private const val Y_AXIS_STEPS = 4
private const val EnterAnimationDurationMs = 350

@Composable
fun NutritionTrendChart(
    modifier: Modifier = Modifier,
    points: List<MacroChartPointUi>,
    range: ProgressRange,
    macroColor: Color,
    unit: String,
    hasData: Boolean = true,
) {
    if (points.isEmpty()) return

    val surfaceColor = MaterialTheme.colorScheme.surface
    val density = LocalDensity.current

    var isVisible by remember(points) { mutableStateOf(false) }
    LaunchedEffect(points) {
        isVisible = true
    }

    val axisLabelColor = colorScheme.onSurfaceVariant

    val enterProgress by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(EnterAnimationDurationMs, easing = FastOutSlowInEasing),
        label = "chartEnterProgress",
    )

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .height(ChartHeight)
            .graphicsLayer {
                alpha = enterProgress
                translationY = with(density) { (ChartHeight / 3).toPx() } * (1f - enterProgress)
            },
    ) {
        val availableWidth = maxWidth

        val stepWidth = remember(availableWidth, points.size, range) {
            when (range) {
                ProgressRange.WEEK -> {
                    availableWidth / 6
                }

                ProgressRange.MONTH -> {
                    val graphWidth = availableWidth - 55.dp
                    graphWidth / 29
                }
            }
        }

        val dotRadius = remember(points.size) {
            when {
                points.size <= 7 -> 4.dp
                points.size <= 15 -> 3.dp
                else -> 2.dp
            }
        }

        val axisMaxY = remember(points) {
            val highest = points.maxOf { it.consumed }
            if (highest <= 0f) 1f else highest * 1.15f
        }

        val consumedPoints = remember(points) {
            points.mapIndexed { index, point -> Point(index.toFloat(), point.consumed) }
        }
        val xAxisData = remember(points, range, stepWidth) {
            AxisData.Builder()
                .axisStepSize(stepWidth)
                .indicatorLineWidth(0.dp)
                .steps(points.size - 1)
                .axisLabelColor(axisLabelColor)
                .axisLineColor(
                    axisLabelColor.copy(alpha = 0.35f),
                )
                .labelData { i ->
                    val dayNumber = i + 1

                    val showLabel = when (range) {
                        ProgressRange.WEEK -> true

                        ProgressRange.MONTH ->
                            dayNumber == 1 ||
                                    dayNumber == 15 ||
                                    i == points.lastIndex
                    }

                    if (showLabel) dayNumber.toString() else ""
                }
                .labelAndAxisLinePadding(14.dp)
                .build()
        }

        val yAxisData = remember(axisMaxY) {
            AxisData.Builder()
                .steps(Y_AXIS_STEPS)
                .indicatorLineWidth(0.dp)
                .labelAndAxisLinePadding(20.dp)
                .axisOffset(20.dp)
                .axisLabelColor(axisLabelColor)
                .axisLineColor(
                    axisLabelColor.copy(alpha = 0.35f),
                )
                .labelData { i ->
                    (i * (axisMaxY / Y_AXIS_STEPS))
                        .roundToInt()
                        .toString()
                }
                .build()
        }

        val lineChartData = remember(consumedPoints, macroColor, unit, xAxisData, yAxisData, dotRadius) {
            LineChartData(
                linePlotData = LinePlotData(
                    lines = listOf(
                        Line(
                            dataPoints = consumedPoints,
                            lineStyle = LineStyle(
                                color = macroColor,
                                lineType = LineType.SmoothCurve(),
                            ),
                            intersectionPoint = IntersectionPoint(
                                color = macroColor,
                                radius = dotRadius,
                            ),
                            selectionHighlightPoint = SelectionHighlightPoint(color = macroColor),
                            shadowUnderLine = ShadowUnderLine(color = macroColor, alpha = 0.28f),
                            selectionHighlightPopUp = SelectionHighlightPopUp(
                                backgroundColor = macroColor,
                                labelColor = Color.White,
                                popUpLabel = { x, _ ->
                                    val index = x.roundToInt().coerceIn(0, points.lastIndex)
                                    "${points[index].consumed.roundToInt()}$unit"
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
            modifier = Modifier
                .fillMaxSize(),
            lineChartData = lineChartData,
        )

        if (!hasData) {
            val widthPx = with(density) { maxWidth.toPx() }
            val heightPx = with(density) { maxHeight.toPx() }
            val rotationDegrees = remember(widthPx, heightPx) {
                Math.toDegrees(atan2(heightPx.toDouble(), widthPx.toDouble())).toFloat()
            }

            Text(
                text = "Log a meal to see your progress",
                modifier = Modifier
                    .align(Alignment.Center)
                    .graphicsLayer(rotationZ = -rotationDegrees),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color =MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
            )
        }
    }
}
