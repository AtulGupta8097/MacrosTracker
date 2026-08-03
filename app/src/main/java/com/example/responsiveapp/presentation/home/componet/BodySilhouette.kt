package com.example.responsiveapp.presentation.home.componet

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

private const val SILHOUETTE_STROKE_RATIO = 0.03f

@Composable
fun BodySilhouette(
    modifier: Modifier = Modifier,
    tint: Color = MaterialTheme.colorScheme.primary,
    alpha: Float = 0.25f,
) {
    Box(
        modifier = modifier.size(96.dp)
    ) {
        Canvas(
            modifier = Modifier.size(96.dp)
        ) {
            val w = size.width
            val h = size.height
            val strokeWidth = w * SILHOUETTE_STROKE_RATIO

            val headRadius = h * 0.11f
            val headCenter = Offset(
                x = w * 0.5f,
                y = h * 0.16f
            )

            val torsoPath = Path().apply {
                moveTo(w * 0.30f, h * 0.34f)

                quadraticBezierTo(
                    w * 0.30f,
                    h * 0.27f,
                    w * 0.42f,
                    h * 0.27f
                )

                lineTo(w * 0.58f, h * 0.27f)

                quadraticBezierTo(
                    w * 0.70f,
                    h * 0.27f,
                    w * 0.70f,
                    h * 0.34f
                )

                lineTo(w * 0.66f, h * 0.60f)
                lineTo(w * 0.62f, h * 0.90f)
                lineTo(w * 0.38f, h * 0.90f)
                lineTo(w * 0.34f, h * 0.60f)

                close()
            }

            drawCircle(
                color = tint.copy(alpha = alpha),
                radius = headRadius,
                center = headCenter,
                style = Stroke(width = strokeWidth)
            )

            drawPath(
                path = torsoPath,
                color = tint.copy(alpha = alpha),
                style = Stroke(width = strokeWidth)
            )
        }
    }
}