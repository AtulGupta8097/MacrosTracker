package com.example.responsiveapp.presentation.barcode

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.LocalLifecycleOwner
import java.util.concurrent.Executors

// ---------------------------------------------------------------------------------------
// Camera permission
// ---------------------------------------------------------------------------------------

enum class CameraPermissionStatus { Requesting, Granted, Denied }

@Stable
class CameraPermissionState(
    val status: CameraPermissionStatus,
    val request: () -> Unit,
    val openSettings: () -> Unit,
)

@Composable
fun rememberCameraPermissionState(): CameraPermissionState {
    val context = LocalContext.current

    var granted by remember { mutableStateOf(context.hasCameraPermission()) }
    var launched by rememberSaveable { mutableStateOf(false) }
    var answered by rememberSaveable { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { result ->
        granted = result
        answered = true
    }

    LaunchedEffect(Unit) {
        if (!granted && !launched) {
            launched = true
            launcher.launch(Manifest.permission.CAMERA)
        }
    }

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        granted = context.hasCameraPermission()
    }

    val status = when {
        granted -> CameraPermissionStatus.Granted
        answered -> CameraPermissionStatus.Denied
        else -> CameraPermissionStatus.Requesting
    }

    return remember(status, launcher, context) {
        CameraPermissionState(
            status = status,
            request = { launcher.launch(Manifest.permission.CAMERA) },
            openSettings = {
                context.startActivity(
                    Intent(
                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.fromParts("package", context.packageName, null),
                    ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                )
            },
        )
    }
}

private fun Context.hasCameraPermission() =
    ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) ==
        PackageManager.PERMISSION_GRANTED

// ---------------------------------------------------------------------------------------
// Camera preview + analysis
// ---------------------------------------------------------------------------------------

// CHANGED (camera lifecycle):
//  * Camera, analyser and executor are created once and released together, in a safe
//    order (analyser cleared -> use cases unbound -> scanner closed -> executor shut
//    down). Before, the camera stayed bound to the Activity after leaving the screen and
//    the executor was shut down while the analyser could still be delivering frames.
//  * `analyzing` really switches ML Kit on/off (it used to be an unused parameter), so no
//    frames are processed while a result is on screen.
//  * a real torch toggle (the old top-bar icon did nothing).
@Composable
fun BarcodeScannerPreview(
    analyzing: Boolean,
    torchEnabled: Boolean,
    onBarcodeDetected: (String) -> Unit,
    onTorchAvailabilityChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val currentOnBarcodeDetected by rememberUpdatedState(onBarcodeDetected)
    val currentOnTorchAvailabilityChanged by rememberUpdatedState(onTorchAvailabilityChanged)

    val previewView = remember {
        PreviewView(context).apply { scaleType = PreviewView.ScaleType.FILL_CENTER }
    }
    val preview = remember { Preview.Builder().build() }
    val imageAnalysis = remember {
        ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()
    }
    val analysisExecutor = remember(lifecycleOwner) { Executors.newSingleThreadExecutor() }
    val analyzer = remember(lifecycleOwner) {
        BarcodeAnalyzer { barcode -> currentOnBarcodeDetected(barcode) }
    }

    var camera by remember { mutableStateOf<Camera?>(null) }

    DisposableEffect(lifecycleOwner) {
        var disposed = false
        var cameraProvider: ProcessCameraProvider? = null
        val providerFuture = ProcessCameraProvider.getInstance(context)

        providerFuture.addListener(
            {
                if (disposed) return@addListener
                val provider = try {
                    providerFuture.get()
                } catch (_: Exception) {
                    return@addListener
                }
                cameraProvider = provider

                preview.surfaceProvider = previewView.surfaceProvider

                val bound = try {
                    provider.unbind(preview, imageAnalysis)
                    provider.bindToLifecycle(
                        lifecycleOwner,
                        CameraSelector.DEFAULT_BACK_CAMERA,
                        preview,
                        imageAnalysis,
                    )
                } catch (_: Exception) {
                    null
                }

                camera = bound
                currentOnTorchAvailabilityChanged(bound?.cameraInfo?.hasFlashUnit() == true)
            },
            ContextCompat.getMainExecutor(context),
        )

        onDispose {
            disposed = true
            camera = null
            imageAnalysis.clearAnalyzer()
            cameraProvider?.unbind(preview, imageAnalysis)
            analyzer.close()
            analysisExecutor.shutdown()
        }
    }

    LaunchedEffect(analyzing, analyzer, analysisExecutor) {
        if (analyzing) {
            imageAnalysis.setAnalyzer(analysisExecutor, analyzer)
        } else {
            imageAnalysis.clearAnalyzer()
        }
    }

    LaunchedEffect(camera, torchEnabled, analyzing) {
        val boundCamera = camera ?: return@LaunchedEffect
        if (boundCamera.cameraInfo.hasFlashUnit()) {
            // The torch is only on while scanning; it is switched off with the result card.
            boundCamera.cameraControl.enableTorch(torchEnabled && analyzing)
        }
    }

    AndroidView(
        modifier = modifier,
        factory = { previewView },
    )
}
