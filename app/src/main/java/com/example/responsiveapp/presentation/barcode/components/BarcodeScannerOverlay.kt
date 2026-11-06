package com.example.responsiveapp.presentation.barcode.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.responsiveapp.presentation.barcode.BarcodeScanStatus
import com.example.responsiveapp.presentation.ui.theme.spacing

@Composable
fun BarcodeScannerOverlay(
    status: BarcodeScanStatus,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize()) {
        ScannerFrame(
            isScanning = status == BarcodeScanStatus.SCANNING,
            modifier = Modifier.align(Alignment.Center)
        )

        if (status == BarcodeScanStatus.INITIALIZING || status == BarcodeScanStatus.SCANNING) {
            ScanningInstructions(
                isInitializing = status == BarcodeScanStatus.INITIALIZING,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(MaterialTheme.spacing.md)
            )
        }
    }
}

@Composable
private fun ScannerFrame(
    isScanning: Boolean,
    modifier: Modifier = Modifier,
) {
    val accent = MaterialTheme.colorScheme.primary
    val transition = rememberInfiniteTransition(label = "laser")
    val laserProgress by transition.animateFloat(
        initialValue = 0.1f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(tween(1600, easing = LinearEasing), RepeatMode.Reverse),
        label = "laserProgress"
    )

    Canvas(
        modifier = modifier
            .widthIn(max = 320.dp)
            .fillMaxWidth(0.72f)
            .aspectRatio(1.5f)
    ) {
        val stroke = 5.dp.toPx()
        val corner = 36.dp.toPx()
        val radius = 18.dp.toPx()
        val w = size.width
        val h = size.height
        val style = Stroke(width = stroke, cap = StrokeCap.Round)
        val arc = Size(radius * 2, radius * 2)

        drawArc(accent, 180f, 90f, false, Offset(0f, 0f), arc, style = style)
        drawArc(accent, 270f, 90f, false, Offset(w - radius * 2, 0f), arc, style = style)
        drawArc(accent, 90f, 90f, false, Offset(0f, h - radius * 2), arc, style = style)
        drawArc(accent, 0f, 90f, false, Offset(w - radius * 2, h - radius * 2), arc, style = style)

        drawLine(accent, Offset(radius, 0f), Offset(corner + radius, 0f), stroke, StrokeCap.Round)
        drawLine(accent, Offset(0f, radius), Offset(0f, corner + radius), stroke, StrokeCap.Round)
        drawLine(accent, Offset(w - radius, 0f), Offset(w - corner - radius, 0f), stroke, StrokeCap.Round)
        drawLine(accent, Offset(w, radius), Offset(w, corner + radius), stroke, StrokeCap.Round)
        drawLine(accent, Offset(radius, h), Offset(corner + radius, h), stroke, StrokeCap.Round)
        drawLine(accent, Offset(0f, h - radius), Offset(0f, h - corner - radius), stroke, StrokeCap.Round)
        drawLine(accent, Offset(w - radius, h), Offset(w - corner - radius, h), stroke, StrokeCap.Round)
        drawLine(accent, Offset(w, h - radius), Offset(w, h - corner - radius), stroke, StrokeCap.Round)

        if (isScanning) {
            val y = h * laserProgress
            drawLine(
                brush = Brush.horizontalGradient(
                    listOf(Color.Transparent, accent, Color.Transparent)
                ),
                start = Offset(corner / 2, y),
                end = Offset(w - corner / 2, y),
                strokeWidth = 3.dp.toPx(),
                cap = StrokeCap.Round
            )
        }
    }
}


@Composable
private fun ScanningInstructions(
    isInitializing: Boolean,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.96f),
        shadowElevation = 4.dp
    ) {
        Column(
            modifier = Modifier.padding(
                horizontal = MaterialTheme.spacing.md,
                vertical = MaterialTheme.spacing.lg
            ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.sm)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.sm)
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    strokeWidth = 3.dp,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = if (isInitializing) "Starting camera..." else "Scanning...",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Text(
                text = "Keep the barcode inside the frame",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
