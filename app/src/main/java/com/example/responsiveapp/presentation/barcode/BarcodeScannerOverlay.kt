package com.example.responsiveapp.presentation.barcode

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.runtime.State
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp

private val FrameMaxWidth = 320.dp
private const val FrameWidthFraction = 0.78f
private const val FrameAspect = 0.62f          // height / width, suits EAN/UPC codes
private const val FrameCenterYFraction = 0.38f // sits above the centre so the bottom card never covers it
private val FrameCorner = 22.dp
private val BracketLength = 34.dp
private val BracketStroke = 4.dp
private val ScrimColor = Color.Black.copy(alpha = 0.58f)

// CHANGED: one Canvas draws the dimmed surround, the cut-out scan window, rounded corner
// brackets and the scan line. The old version stacked many Boxes and read the animated
// value while composing, so the whole overlay recomposed every frame. Here the animated
// value is only read inside the draw lambda (draw phase only), and the infinite animation
// exists only while `scanning` is true.
@Composable
fun BarcodeScannerOverlay(
    scanning: Boolean,
    modifier: Modifier = Modifier,
) {
    val accent = MaterialTheme.colorScheme.primary

    val lineProgress: State<Float> = if (scanning) {
        rememberInfiniteTransition(label = "scanLine").animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 1600, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse,
            ),
            label = "scanLineProgress",
        )
    } else {
        remember { mutableStateOf(0f) }
    }

    Canvas(
        modifier = modifier.graphicsLayer {
            // Needed so BlendMode.Clear punches a hole in the scrim instead of the window.
            compositingStrategy = CompositingStrategy.Offscreen
        },
    ) {
        val frameWidth = minOf(size.width * FrameWidthFraction, FrameMaxWidth.toPx())
        val frameHeight = frameWidth * FrameAspect
        val topLeft = Offset(
            x = (size.width - frameWidth) / 2f,
            y = size.height * FrameCenterYFraction - frameHeight / 2f,
        )
        val frameSize = Size(frameWidth, frameHeight)
        val corner = FrameCorner.toPx()

        drawRect(color = ScrimColor)
        drawRoundRect(
            color = Color.Black,
            topLeft = topLeft,
            size = frameSize,
            cornerRadius = CornerRadius(corner),
            blendMode = BlendMode.Clear,
        )

        // Faint outline so the window reads as a frame, brackets carry the accent.
        drawRoundRect(
            color = Color.White.copy(alpha = 0.18f),
            topLeft = topLeft,
            size = frameSize,
            cornerRadius = CornerRadius(corner),
            style = Stroke(width = 1.dp.toPx()),
        )

        drawBrackets(topLeft, frameSize, corner, accent)

        if (scanning) {
            val inset = corner
            val y = topLeft.y + inset + (frameHeight - 2 * inset) * lineProgress.value
            drawLine(
                brush = Brush.horizontalGradient(
                    colors = listOf(Color.Transparent, accent, Color.Transparent),
                    startX = topLeft.x + inset,
                    endX = topLeft.x + frameWidth - inset,
                ),
                start = Offset(topLeft.x + inset, y),
                end = Offset(topLeft.x + frameWidth - inset, y),
                strokeWidth = 2.5.dp.toPx(),
                cap = StrokeCap.Round,
            )
        }
    }
}

private fun DrawScope.drawBrackets(topLeft: Offset, frameSize: Size, radius: Float, color: Color) {
    val length = BracketLength.toPx()
    val stroke = Stroke(width = BracketStroke.toPx(), cap = StrokeCap.Round)
    val left = topLeft.x
    val top = topLeft.y
    val right = topLeft.x + frameSize.width
    val bottom = topLeft.y + frameSize.height

    // (x, y) is the corner point; dx / dy point INTO the frame.
    fun bracket(x: Float, y: Float, dx: Float, dy: Float) {
        val path = Path().apply {
            moveTo(x, y + dy * length)
            lineTo(x, y + dy * radius)
            quadraticTo(x, y, x + dx * radius, y)
            lineTo(x + dx * length, y)
        }
        drawPath(path = path, color = color, style = stroke)
    }

    bracket(left, top, 1f, 1f)
    bracket(right, top, -1f, 1f)
    bracket(left, bottom, 1f, -1f)
    bracket(right, bottom, -1f, -1f)
}
