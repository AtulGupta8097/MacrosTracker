package com.macrivo.app.presentation.barcode

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.macrivo.app.core.permission.rememberPermissionHandler

@Composable
fun BarcodeScanScreen(
    onBack: () -> Unit,
    onLogged: () -> Unit,
    viewModel: BarcodeScanViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val permission = rememberPermissionHandler(Manifest.permission.CAMERA)
    val snackbarHostState = remember { SnackbarHostState() }
    var isInfoVisible by rememberSaveable { mutableStateOf(false) }

    val actions = remember(viewModel, permission) {
        BarcodeScanActions(
            onBack = viewModel::onNavigateBack,
            onCameraReady = viewModel::onCameraReady,
            onCameraDisposed = viewModel::onCameraDisposed,
            onPreviewStreaming = viewModel::onPreviewStreaming,
            onRetryCamera = viewModel::onRetryCamera,
            onToggleTorch = viewModel::onToggleTorch,
            onSwitchCamera = viewModel::onSwitchCamera,
            onZoomClick = viewModel::onZoomCycle,
            onRequestPermission = permission.request,
            onOpenSettings = permission.openSettings,
            onViewNutrition = viewModel::onViewNutrition,
            onScanAnother = viewModel::onScanAnother,
            onRetryLookup = viewModel::onRetryLookup,
            onServingSelected = viewModel::onServingSelected,
            onQuantityIncrement = viewModel::onQuantityIncrement,
            onQuantityDecrement = viewModel::onQuantityDecrement,
            onLogFood = viewModel::onLogFood,
            onInfo = { isInfoVisible = true },
        )
    }

    LaunchedEffect(Unit) {
        if (!context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
            viewModel.onCameraError(IllegalStateException("No camera available"))
        }
    }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is BarcodeScanEvent.ShowMessage -> snackbarHostState.showSnackbar(event.message)
                BarcodeScanEvent.NavigateBack -> onBack()
                BarcodeScanEvent.Logged -> onLogged()
            }
        }
    }

    BackHandler(onBack = viewModel::onNavigateBack)

    Box(modifier = Modifier.fillMaxSize()) {
        BarcodeScanContent(
            state = state,
            permissionStatus = permission.status,
            actions = actions
        )

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
        )
    }

    if (isInfoVisible) {
        AlertDialog(
            onDismissRequest = { isInfoVisible = false },
            title = { Text("Scanning tips") },
            text = {
                Text(
                    "Hold your phone steady and keep the barcode inside the frame. " +
                        "Use the flashlight in dim light. " +
                        "EAN-8, EAN-13 and UPC-A product barcodes are supported."
                )
            },
            confirmButton = {
                TextButton(onClick = { isInfoVisible = false }) { Text("Got it") }
            }
        )
    }
}
