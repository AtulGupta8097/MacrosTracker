package com.example.responsiveapp.presentation.home.componet.nutrition

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.responsiveapp.presentation.ui.theme.ResponsiveAppTheme

private val TrackHeight = 8.dp

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
            durationMillis = 700,
        ),
        label = "caloriesLinearProgress",
    )

    LinearProgressIndicator(
        progress = {
            animatedProgress
        },
        modifier = modifier
            .fillMaxWidth()
            .height(TrackHeight),
        color = progressColor,
        trackColor = trackColor,
        strokeCap = StrokeCap.Round,
    )
}

@Preview
@Composable
private fun PrevLinearProgress() {
    ResponsiveAppTheme {
        LinearProgress(
            progress = 0.5f,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}