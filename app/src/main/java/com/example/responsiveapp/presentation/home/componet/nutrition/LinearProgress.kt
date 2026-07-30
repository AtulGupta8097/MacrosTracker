package com.example.responsiveapp.presentation.home.componet.nutrition

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.responsiveapp.presentation.ui.theme.ResponsiveAppTheme

private val TrackHeight = 8.dp
private const val ProgressAnimationDurationMillis = 700

@Composable
fun LinearProgress(
    progress: Float,
    modifier: Modifier = Modifier,
    trackColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    progressColor: Color = MaterialTheme.colorScheme.primary,
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress.coerceIn(0f, 1f),
        animationSpec = tween(
            durationMillis = ProgressAnimationDurationMillis,
        ),
        label = "linearProgress",
    )

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(TrackHeight),
    ) {

        val radius = size.height / 2f

        drawRoundRect(
            color = trackColor,
            size = size,
            cornerRadius = CornerRadius(radius),
        )

        drawRoundRect(
            color = progressColor,
            size = Size(
                width = size.width * animatedProgress,
                height = size.height,
            ),
            cornerRadius = CornerRadius(radius),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun LinearProgressPreview() {
    ResponsiveAppTheme {
        LinearProgress(
            progress = 0.65f,
        )
    }
}