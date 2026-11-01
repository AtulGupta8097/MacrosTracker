package com.example.responsiveapp.presentation.barcode

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FlashOff
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.responsiveapp.presentation.ui.theme.spacing

private val ButtonSize = 44.dp

// CHANGED: respects the status bar (edge-to-edge) and the flash icon is now a working torch
// toggle. It is only shown when the device has a flash unit; a spacer keeps the title centred.
@Composable
fun BarcodeScannerTopBar(
    onBack: () -> Unit,
    showTorch: Boolean,
    torchOn: Boolean,
    onTorchToggle: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = MaterialTheme.spacing.md, vertical = MaterialTheme.spacing.sm),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        ScannerIconButton(
            icon = Icons.Default.Close,
            contentDescription = "Close",
            onClick = onBack,
        )

        Text(
            text = "Scan barcode",
            color = Color.White,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
        )

        if (showTorch) {
            ScannerIconButton(
                icon = if (torchOn) Icons.Default.FlashOn else Icons.Default.FlashOff,
                contentDescription = if (torchOn) "Turn flashlight off" else "Turn flashlight on",
                onClick = onTorchToggle,
                highlighted = torchOn,
            )
        } else {
            Spacer(modifier = Modifier.size(ButtonSize))
        }
    }
}

@Composable
private fun ScannerIconButton(
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
    highlighted: Boolean = false,
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .size(ButtonSize)
            .background(
                color = if (highlighted) MaterialTheme.colorScheme.primary
                else Color.Black.copy(alpha = 0.45f),
                shape = CircleShape,
            ),
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = if (highlighted) MaterialTheme.colorScheme.onPrimary else Color.White,
        )
    }
}
