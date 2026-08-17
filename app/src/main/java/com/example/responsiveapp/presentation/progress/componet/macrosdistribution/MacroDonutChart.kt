package com.example.responsiveapp.presentation.progress.componet.macrosdistribution

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.responsiveapp.presentation.progress.model.MacroDistributionUiState
import com.example.responsiveapp.presentation.ui.theme.CarbsColor
import com.example.responsiveapp.presentation.ui.theme.FatColor
import com.example.responsiveapp.presentation.ui.theme.ProteinColor
import kotlin.math.cos
import kotlin.math.sin

private data class DonutArc(
    val startAngle: Float,
    val sweepAngle: Float,
    val percent: Int,
    val color: Color,
)

@Composable
fun MacroDonutChart(
    state: MacroDistributionUiState,
    modifier: Modifier = Modifier,
) {
    val slices = remember(state) {
        listOf(
            Triple(state.protein.calorieShare, state.protein.percentOfCalories, ProteinColor),
            Triple(state.carbs.calorieShare, state.carbs.percentOfCalories, CarbsColor),
            Triple(state.fats.calorieShare, state.fats.percentOfCalories, FatColor),
        ).filter { (share, _, _) -> share > 0f }
    }

    val arcs = remember(slices) {
        val gapDegrees = if (slices.size > 1) 3f else 0f
        var currentAngle = -90f
        slices.map { (share, percent, color) ->
            val sweep = (share * 360f - gapDegrees).coerceAtLeast(0f)
            val arc = DonutArc(startAngle = currentAngle, sweepAngle = sweep, percent = percent, color = color)
            currentAngle += share * 360f
            arc
        }
    }

    val density = LocalDensity.current

    BoxWithConstraints(modifier = modifier) {
        val diameterPx = with(density) { minOf(maxWidth, maxHeight).toPx() }
        val strokeWidthPx = diameterPx * 0.19f
        val radiusPx = (diameterPx - strokeWidthPx) / 2f

        Canvas(modifier = Modifier.fillMaxSize()) {
            val topLeft = Offset(
                (size.width - radiusPx * 2) / 2f,
                (size.height - radiusPx * 2) / 2f,
            )
            val arcSize = Size(radiusPx * 2, radiusPx * 2)
            arcs.forEach { arc ->
                drawArc(
                    color = arc.color,
                    startAngle = arc.startAngle,
                    sweepAngle = arc.sweepAngle,
                    useCenter = false,
                    topLeft = topLeft,
                    size = arcSize,
                    style = Stroke(width = strokeWidthPx, cap = StrokeCap.Round),
                )
            }
        }

        arcs.forEach { arc ->
            val midAngleRad = Math.toRadians((arc.startAngle + arc.sweepAngle / 2).toDouble())
            val xPx = (diameterPx / 2f) + (radiusPx * cos(midAngleRad)).toFloat()
            val yPx = (diameterPx / 2f) + (radiusPx * sin(midAngleRad)).toFloat()
            Text(
                text = "${arc.percent}%",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.offset(
                    x = with(density) { xPx.toDp() } - 16.dp,
                    y = with(density) { yPx.toDp() } - 9.dp,
                ),
            )
        }
    }
}