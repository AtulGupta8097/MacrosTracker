package com.example.responsiveapp.presentation.home.componet

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.responsiveapp.presentation.ui.theme.ResponsiveAppTheme
import com.example.responsiveapp.presentation.ui.theme.spacing

private const val PulseMaxScale = 1.12f
private const val PulseDurationMs = 1100

private val IconNumberGap = 6.dp
private val StreakCountMinWidth = 20.dp

@Composable
fun StreakIndicator(
    streakCount: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val hasStreak = streakCount > 0

    val infiniteTransition = rememberInfiniteTransition(
        label = "streakPulse",
    )

    val pulse by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = PulseMaxScale,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = PulseDurationMs,
                easing = FastOutSlowInEasing,
            ),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "streakPulseScale",
    )

    val flameTint = if (hasStreak) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
    }

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .background(MaterialTheme.colorScheme.primaryContainer)
            .clickable(onClick = onClick)
            .padding(
                horizontal = MaterialTheme.spacing.sm +
                        MaterialTheme.spacing.xxs,
                vertical = MaterialTheme.spacing.xs,
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            contentAlignment = Alignment.Center,
        ) {
            if (hasStreak) {
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .scale(pulse)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.primary.copy(
                                        alpha = 0.35f,
                                    ),
                                    MaterialTheme.colorScheme.primary.copy(
                                        alpha = 0f,
                                    ),
                                ),
                            ),
                            shape = CircleShape,
                        ),
                )
            }

            Icon(
                imageVector = Icons.Default.LocalFireDepartment,
                contentDescription = "Current streak",
                tint = flameTint,
                modifier = Modifier
                    .size(20.dp)
                    .scale(if (hasStreak) pulse else 1f),
            )
        }

        Text(
            text = streakCount.toString(),
            modifier = Modifier
                .padding(start = IconNumberGap)
                .widthIn(min = StreakCountMinWidth),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleMedium.copy(
                fontSize = 18.sp,
                letterSpacing = 0.sp,
                fontFeatureSettings = "tnum, lnum",
            ),
            fontWeight = FontWeight.Bold,
            color = flameTint,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun StreakIndicatorPreview() {
    ResponsiveAppTheme {
        Row {
            StreakIndicator(
                streakCount = 7,
                onClick = {},
            )

            StreakIndicator(
                streakCount = 0,
                onClick = {},
                modifier = Modifier.padding(start = 12.dp),
            )
        }
    }
}
