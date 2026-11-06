package com.example.responsiveapp.presentation.barcode.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FlashOff
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.outlined.FlipCameraAndroid
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.responsiveapp.presentation.ui.theme.spacing

@Composable
fun CameraControls(
    enabled: Boolean,
    hasFlash: Boolean,
    isTorchEnabled: Boolean,
    zoomRatio: Float,
    minZoomRatio: Float,
    maxZoomRatio: Float,
    onToggleTorch: () -> Unit,
    onSwitchCamera: () -> Unit,
    onZoomClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.sm + MaterialTheme.spacing.xs),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ScannerControlButton(
            enabled = enabled && hasFlash,
            active = isTorchEnabled,
            onClick = onToggleTorch
        ) {
            Icon(
                imageVector = if (isTorchEnabled) Icons.Filled.FlashOn else Icons.Filled.FlashOff,
                contentDescription = if (isTorchEnabled) "Turn flashlight off" else "Turn flashlight on",
                modifier = Modifier.size(24.dp)
            )
        }

        ScannerControlButton(
            enabled = enabled,
            active = false,
            onClick = onSwitchCamera
        ) {
            Icon(
                imageVector = Icons.Outlined.FlipCameraAndroid,
                contentDescription = "Switch camera",
                modifier = Modifier.size(24.dp)
            )
        }

        ScannerControlButton(
            enabled = enabled && maxZoomRatio > minZoomRatio,
            active = false,
            onClick = onZoomClick
        ) {
            Text(
                text = "%.1f×".format(zoomRatio),
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
            )
        }
    }
}

@Composable
private fun ScannerControlButton(
    enabled: Boolean,
    active: Boolean,
    onClick: () -> Unit,
    content: @Composable () -> Unit,
) {
    val container = when {
        active -> MaterialTheme.colorScheme.primary
        enabled -> Color.White.copy(alpha = 0.95f)
        else -> Color.Black.copy(alpha = 0.4f)
    }
    val contentColor = when {
        active -> MaterialTheme.colorScheme.onPrimary
        enabled -> Color.Black
        else -> Color.White.copy(alpha = 0.5f)
    }

    Surface(
        onClick = onClick,
        enabled = enabled,
        shape = CircleShape,
        color = container,
        contentColor = contentColor,
        modifier = Modifier.size(48.dp)
    ) {
        Box(contentAlignment = Alignment.Center) { content() }
    }
}
