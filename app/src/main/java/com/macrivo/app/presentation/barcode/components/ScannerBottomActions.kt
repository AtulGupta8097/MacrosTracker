package com.macrivo.app.presentation.barcode.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.QrCodeScanner
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.macrivo.app.presentation.ui.theme.spacing

@Composable
fun ScannerBottomActions(
    onScanAnother: () -> Unit,
    modifier: Modifier = Modifier,
    primaryLabel: String? = null,
    primaryIcon: ImageVector? = null,
    onPrimary: () -> Unit = {},
    isPrimaryLoading: Boolean = false,
    enabled: Boolean = true,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.sm + MaterialTheme.spacing.xs)
    ) {
        if (primaryLabel != null) {
            Button(
                onClick = onPrimary,
                enabled = enabled,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = MaterialTheme.shapes.medium,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
                    disabledContentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                if (isPrimaryLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    if (primaryIcon != null) {
                        Icon(primaryIcon, contentDescription = null, modifier = Modifier.size(20.dp))
                    }
                    Text(
                        text = primaryLabel,
                        modifier = Modifier.padding(start = MaterialTheme.spacing.sm),
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }
        }

        OutlinedButton(
            onClick = onScanAnother,
            enabled = enabled,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = MaterialTheme.shapes.medium
        ) {
            Icon(Icons.Outlined.QrCodeScanner, contentDescription = null, modifier = Modifier.size(18.dp))
            Text(
                text = "Scan Another",
                modifier = Modifier.padding(start = MaterialTheme.spacing.sm),
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold)
            )
        }
    }
}
