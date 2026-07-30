package com.example.responsiveapp.presentation.home.componet.nutrition

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.responsiveapp.presentation.ui.theme.ResponsiveAppTheme
import kotlin.math.roundToInt

private const val RingAnimationDurationMillis = 700

private const val FullCircleDegrees = 360f
private const val StartAngleDegrees = -90f

private const val StrokeWidthRatio = 0.09f
private const val ArcPaddingRatio = 0.06f

private val MinRingSize = 64.dp
private val MaxRingSize = 160.dp

@Composable
fun CaloriesProgressRing(
    progress: Float,
    modifier: Modifier = Modifier,
    trackColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    progressColor: Color = MaterialTheme.colorScheme.primary,
) {
    val clampedProgress = progress.coerceIn(0f, 1f)

    val animatedProgress by animateFloatAsState(
        targetValue = clampedProgress,
        animationSpec = tween(
            durationMillis = RingAnimationDurationMillis,
        ),
        label = "caloriesRingProgress",
    )

    val percentageLabel =
        "${(clampedProgress * 100).roundToInt()}%"

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .sizeIn(
                minWidth = MinRingSize,
                maxWidth = MaxRingSize,
            )
            .semantics {
                contentDescription =
                    "Calories progress $percentageLabel of daily goal"
            },
        contentAlignment = Alignment.Center,
    ) {

        Canvas(
            modifier = Modifier.fillMaxSize(),
        ) {

            val strokeWidth =
                size.minDimension * StrokeWidthRatio

            val arcPadding =
                size.minDimension * ArcPaddingRatio

            val arcDiameter =
                size.minDimension -
                    strokeWidth -
                    (arcPadding * 2)

            val arcSize =
                Size(
                    width = arcDiameter,
                    height = arcDiameter,
                )

            val topLeft =
                Offset(
                    x = (size.width - arcDiameter) / 2f,
                    y = (size.height - arcDiameter) / 2f,
                )

            val stroke =
                Stroke(
                    width = strokeWidth,
                    cap = StrokeCap.Round,
                )

            drawArc(
                color = trackColor,
                startAngle = StartAngleDegrees,
                sweepAngle = FullCircleDegrees,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = stroke,
            )

            drawArc(
                color = progressColor,
                startAngle = StartAngleDegrees,
                sweepAngle = FullCircleDegrees * animatedProgress,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = stroke,
            )
        }

        Text(
            text = percentageLabel,
            style = MaterialTheme.typography.titleMedium
                .copy(
                    fontWeight = FontWeight.Bold,
                ),
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CaloriesProgressRingPreview() {
    ResponsiveAppTheme {
        CaloriesProgressRing(
            progress = 0.72f,
        )
    }
}