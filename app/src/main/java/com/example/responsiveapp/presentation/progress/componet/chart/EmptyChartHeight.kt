package com.example.responsiveapp.presentation.progress.componet.chart

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlin.math.atan2

private val EmptyChartHeight = 190.dp

@Composable
fun NutritionChartEmptyState(
    macroColor: Color,
    modifier: Modifier = Modifier,
) {
    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .height(EmptyChartHeight),
    ) {
        val density = LocalDensity.current
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
            color = macroColor.copy(alpha = 0.55f),
            textAlign = TextAlign.Center,
        )
    }
}