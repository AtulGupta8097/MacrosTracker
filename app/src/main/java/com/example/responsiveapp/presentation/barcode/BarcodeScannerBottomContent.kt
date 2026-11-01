package com.example.responsiveapp.presentation.barcode

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.responsiveapp.domain.model.Serving
import com.example.responsiveapp.presentation.ui.theme.spacing

// Chooses what sits at the bottom of the scanner for the current state. contentKey is the
// state TYPE, so changing quantity inside `Found` does not replay the transition.
@Composable
fun BarcodeScannerBottomContent(
    uiState: BarcodeScanUiState,
    onServingSelected: (Serving) -> Unit,
    onQuantityChanged: (Float) -> Unit,
    onAddToLog: () -> Unit,
    onScanAgain: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AnimatedContent(
        targetState = uiState,
        modifier = modifier,
        contentKey = { it::class },
        transitionSpec = {
            (fadeIn(tween(220)) + slideInVertically(tween(260)) { it / 5 }) togetherWith
                fadeOut(tween(120))
        },
        label = "barcodeBottomContent",
    ) { state ->
        when (state) {
            is BarcodeScanUiState.Scanning -> BarcodeScannerInstructions()

            is BarcodeScanUiState.Added -> Unit

            is BarcodeScanUiState.Loading -> BarcodeScannerLoadingState()

            is BarcodeScanUiState.NotFound -> BarcodeScannerErrorState(
                icon = Icons.Default.SearchOff,
                title = "No match found",
                message = "This barcode isn't in FatSecret's database yet.",
                actionLabel = "Scan again",
                onAction = onScanAgain,
            )

            is BarcodeScanUiState.Error -> BarcodeScannerErrorState(
                icon = Icons.Default.ErrorOutline,
                title = "Something went wrong",
                message = state.message,
                actionLabel = "Try again",
                onAction = onScanAgain,
            )

            is BarcodeScanUiState.Found -> BarcodeScannerResultContent(
                state = state,
                onServingSelected = onServingSelected,
                onQuantityChanged = onQuantityChanged,
                onAddToLog = onAddToLog,
                onScanAgain = onScanAgain,
            )
        }
    }
}

@Composable
fun BarcodeScannerInstructions(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(bottom = MaterialTheme.spacing.xl),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "Point the camera at a product barcode",
            modifier = Modifier
                .background(Color.Black.copy(alpha = 0.55f), RoundedCornerShape(50))
                .padding(horizontal = MaterialTheme.spacing.md, vertical = MaterialTheme.spacing.sm),
            color = Color.White,
            style = MaterialTheme.typography.bodySmall,
        )
    }
}

@Composable
fun BarcodeScannerLoadingState(modifier: Modifier = Modifier) {
    BarcodeScannerSheet(modifier = modifier) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            CircularProgressIndicator(
                modifier = Modifier.size(28.dp),
                strokeWidth = 3.dp,
                color = MaterialTheme.colorScheme.primary,
            )
            Column(modifier = Modifier.padding(start = MaterialTheme.spacing.md)) {
                Text(
                    text = "Looking up product",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    text = "Fetching nutrition from FatSecret",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@Composable
fun BarcodeScannerErrorState(
    icon: ImageVector,
    title: String,
    message: String,
    actionLabel: String,
    onAction: () -> Unit,
    modifier: Modifier = Modifier,
) {
    BarcodeScannerSheet(modifier = modifier) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.xs),
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f), CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                )
            }
            Text(
                text = title,
                modifier = Modifier.padding(top = MaterialTheme.spacing.xs),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                text = message,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
            Button(
                onClick = onAction,
                modifier = Modifier.padding(top = MaterialTheme.spacing.md),
                shape = RoundedCornerShape(50),
            ) {
                Text(actionLabel)
            }
        }
    }
}

// Shared bottom sheet surface. Handles the navigation-bar inset in one place.
@Composable
internal fun BarcodeScannerSheet(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 3.dp,
        shadowElevation = 12.dp,
    ) {
        Box(
            modifier = Modifier
                .navigationBarsPadding()
                .padding(MaterialTheme.spacing.lg),
        ) {
            content()
        }
    }
}
