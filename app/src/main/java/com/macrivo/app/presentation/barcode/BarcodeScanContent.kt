package com.macrivo.app.presentation.barcode

import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LifecycleOwner
import com.macrivo.app.core.permission.PermissionStatus
import com.macrivo.app.domain.model.Serving
import com.macrivo.app.presentation.barcode.components.BarcodeCameraPreview
import com.macrivo.app.presentation.barcode.components.BarcodePermissionContent
import com.macrivo.app.presentation.barcode.components.BarcodeScannerOverlay
import com.macrivo.app.presentation.barcode.components.BarcodeScannerTopBar
import com.macrivo.app.presentation.barcode.components.CameraControls
import com.macrivo.app.presentation.barcode.components.CameraUnavailableContent
import com.macrivo.app.presentation.barcode.components.ProductFoundContent
import com.macrivo.app.presentation.barcode.components.ProductNutritionContent
import com.macrivo.app.presentation.barcode.components.ScannerErrorContent
import com.macrivo.app.presentation.barcode.components.ScannerLoadingOverlay
import com.macrivo.app.presentation.ui.theme.spacing

@Immutable
class BarcodeScanActions(
    val onBack: () -> Unit,
    val onCameraReady: (LifecycleCameraController, LifecycleOwner) -> Unit,
    val onCameraDisposed: () -> Unit,
    val onPreviewStreaming: () -> Unit,
    val onRetryCamera: () -> Unit,
    val onToggleTorch: () -> Unit,
    val onSwitchCamera: () -> Unit,
    val onZoomClick: () -> Unit,
    val onRequestPermission: () -> Unit,
    val onOpenSettings: () -> Unit,
    val onViewNutrition: () -> Unit,
    val onScanAnother: () -> Unit,
    val onRetryLookup: () -> Unit,
    val onServingSelected: (Serving) -> Unit,
    val onQuantityIncrement: () -> Unit,
    val onQuantityDecrement: () -> Unit,
    val onLogFood: () -> Unit,
    val onInfo: () -> Unit,
)

@Composable
fun BarcodeScanContent(
    state: BarcodeScanUiState,
    permissionStatus: PermissionStatus,
    actions: BarcodeScanActions,
    modifier: Modifier = Modifier,
) {
    val food = state.food
    val serving = state.selectedServing
    val nutrition = state.scaledNutrition
    val isDetailsStage = state.status == BarcodeScanStatus.FOOD_DETAILS ||
        state.status == BarcodeScanStatus.LOGGING ||
        state.status == BarcodeScanStatus.ADDED

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            BarcodeScannerTopBar(
                title = "Scan Barcode",
                subtitle = "Point your camera at a product barcode",
                onBack = actions.onBack,
                onInfo = actions.onInfo
            )

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = MaterialTheme.spacing.sm)
                    .padding(bottom = MaterialTheme.spacing.sm)
                    .clip(RoundedCornerShape(24.dp))
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                when {
                    permissionStatus != PermissionStatus.GRANTED -> BarcodePermissionContent(
                        status = permissionStatus,
                        onRequestPermission = actions.onRequestPermission,
                        onOpenSettings = actions.onOpenSettings
                    )

                    state.status == BarcodeScanStatus.CAMERA_UNAVAILABLE -> CameraUnavailableContent(
                        onRetry = actions.onRetryCamera
                    )

                    else -> ScannerStage(state = state, actions = actions)
                }
            }
        }

        if (state.status == BarcodeScanStatus.PRODUCT_FOUND && food != null && nutrition != null) {
            ProductFoundContent(
                foodName = food.name,
                brand = food.brand,
                foodId = food.id,
                nutrition = nutrition,
                onViewNutrition = actions.onViewNutrition,
                onScanAnother = actions.onScanAnother,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }

        if (isDetailsStage && food != null && serving != null && nutrition != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            ) {
                BarcodeScannerTopBar(
                    title = "Food Details",
                    subtitle = null,
                    onBack = actions.onBack
                )

                ProductNutritionContent(
                    food = food,
                    selectedServing = serving,
                    quantity = state.quantity,
                    nutrition = nutrition,
                    isLogging = state.status == BarcodeScanStatus.LOGGING,
                    onServingSelected = actions.onServingSelected,
                    onQuantityIncrement = actions.onQuantityIncrement,
                    onQuantityDecrement = actions.onQuantityDecrement,
                    onLogFood = actions.onLogFood,
                    onScanAnother = actions.onScanAnother,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun ScannerStage(
    state: BarcodeScanUiState,
    actions: BarcodeScanActions,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize()) {
        BarcodeCameraPreview(
            onCameraReady = actions.onCameraReady,
            onCameraDisposed = actions.onCameraDisposed,
            onPreviewStreaming = actions.onPreviewStreaming,
            modifier = Modifier.fillMaxSize()
        )

        val isCameraActive = state.status == BarcodeScanStatus.INITIALIZING ||
            state.status == BarcodeScanStatus.SCANNING

        if (!isCameraActive) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.6f))
            )
        }

        BarcodeScannerOverlay(status = state.status)

        CameraControls(
            enabled = state.status == BarcodeScanStatus.SCANNING,
            hasFlash = state.hasFlash,
            isTorchEnabled = state.isTorchEnabled,
            zoomRatio = state.zoomRatio,
            minZoomRatio = state.minZoomRatio,
            maxZoomRatio = state.maxZoomRatio,
            onToggleTorch = actions.onToggleTorch,
            onSwitchCamera = actions.onSwitchCamera,
            onZoomClick = actions.onZoomClick,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(MaterialTheme.spacing.md)
        )

        val bottomModifier = Modifier
            .align(Alignment.BottomCenter)
            .padding(MaterialTheme.spacing.md)

        when (state.status) {
            BarcodeScanStatus.FETCHING_PRODUCT -> ScannerLoadingOverlay(
                barcode = state.scannedBarcode.orEmpty(),
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(horizontal = MaterialTheme.spacing.lg)
            )

            BarcodeScanStatus.NOT_FOUND -> ScannerErrorContent(
                title = "Product not found",
                message = "We couldn't find a match for barcode ${state.scannedBarcode.orEmpty()}.",
                onScanAnother = actions.onScanAnother,
                modifier = bottomModifier
            )

            BarcodeScanStatus.ERROR -> ScannerErrorContent(
                title = "Something went wrong",
                message = state.errorMessage ?: "Please try again.",
                onScanAnother = actions.onScanAnother,
                onRetry = actions.onRetryLookup,
                modifier = bottomModifier
            )

            else -> Unit
        }
    }
}
