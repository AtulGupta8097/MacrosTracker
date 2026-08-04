package com.example.responsiveapp.presentation.home.componet.skeleton

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

private const val SHIMMER_TRAVEL_DISTANCE = 600f
private const val SHIMMER_DURATION_MS = 1100

@Composable
private fun rememberShimmerBrush(): Brush {
    val transition = rememberInfiniteTransition(
        label = "ShimmerTransition",
    )

    val translate by transition.animateFloat(
        initialValue = 0f,
        targetValue = SHIMMER_TRAVEL_DISTANCE,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = SHIMMER_DURATION_MS,
                easing = LinearEasing,
            ),
            repeatMode = RepeatMode.Restart,
        ),
        label = "ShimmerTranslate",
    )

    val baseColor = MaterialTheme.colorScheme.surfaceVariant

    return Brush.linearGradient(
        colors = listOf(
            baseColor.copy(alpha = 0.55f),
            baseColor.copy(alpha = 0.25f),
            baseColor.copy(alpha = 0.55f),
        ),
        start = Offset(
            x = translate - SHIMMER_TRAVEL_DISTANCE / 2,
            y = 0f,
        ),
        end = Offset(
            x = translate,
            y = 0f,
        ),
    )
}

@Composable
fun SkeletonBox(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(8.dp),
) {
    Box(
        modifier = modifier
            .clip(shape)
            .background(
                brush = rememberShimmerBrush(),
            ),
    )
}