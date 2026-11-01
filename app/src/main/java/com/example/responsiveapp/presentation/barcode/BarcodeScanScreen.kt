package com.example.responsiveapp.presentation.barcode

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.responsiveapp.presentation.commoncomponent.CustomToast
import com.example.responsiveapp.presentation.commoncomponent.ErrorToast

@Composable
fun BarcodeScanScreen(
    onBack: () -> Unit,
    onLogged: () -> Unit,
    viewModel: BarcodeScanViewModel = hiltViewModel(),
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()
    val currentOnLogged by rememberUpdatedState(onLogged)
    val permission = rememberCameraPermissionState()

    var torchOn by rememberSaveable { mutableStateOf(false) }
    var hasFlash by remember { mutableStateOf(false) }

    var toastMessage by remember { mutableStateOf("") }
    var toastVisible by remember { mutableStateOf(false) }

    val errorToast = remember { ErrorToast() }

    val isAdded = uiState is BarcodeScanUiState.Added
    LaunchedEffect(isAdded) {
        if (isAdded) currentOnLogged()
    }

    // CHANGED: the ViewModel emitted "Couldn't add to log" but nothing ever collected it.
    LaunchedEffect(viewModel) {
        viewModel.events.collect { message ->
            toastMessage = message
            toastVisible = true
        }
    }

    val cameraGranted = permission.status == CameraPermissionStatus.Granted
    val scanning = uiState is BarcodeScanUiState.Scanning

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
    ) {
        when (permission.status) {
            CameraPermissionStatus.Granted -> {
                BarcodeScannerPreview(
                    analyzing = scanning,
                    torchEnabled = torchOn,
                    onBarcodeDetected = viewModel::onBarcodeDetected,
                    onTorchAvailabilityChanged = { hasFlash = it },
                    modifier = Modifier.fillMaxSize(),
                )
                BarcodeScannerOverlay(
                    scanning = scanning,
                    modifier = Modifier.fillMaxSize(),
                )
            }

            CameraPermissionStatus.Denied -> BarcodeScannerPermissionDenied(
                onRequestPermission = permission.request,
                onOpenSettings = permission.openSettings,
                modifier = Modifier.fillMaxSize(),
            )

            CameraPermissionStatus.Requesting -> Unit
        }

        BarcodeScannerTopBar(
            onBack = onBack,
            showTorch = cameraGranted && hasFlash,
            torchOn = torchOn,
            onTorchToggle = { torchOn = !torchOn },
            modifier = Modifier.align(Alignment.TopCenter),
        )

        if (cameraGranted) {
            BarcodeScannerBottomContent(
                uiState = uiState,
                onServingSelected = viewModel::onServingSelected,
                onQuantityChanged = viewModel::onQuantityChanged,
                onAddToLog = viewModel::onAddToLog,
                onScanAgain = viewModel::onScanAgain,
                modifier = Modifier.align(Alignment.BottomCenter),
            )
        }

        CustomToast(
            message = toastMessage,
            type = errorToast,
            visibility = toastVisible,
            onDismiss = { toastVisible = false },
        )
    }
}
