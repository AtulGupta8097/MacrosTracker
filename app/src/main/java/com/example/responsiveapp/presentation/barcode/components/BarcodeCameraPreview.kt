package com.example.responsiveapp.presentation.barcode.components

import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.compose.LocalLifecycleOwner

@Composable
fun BarcodeCameraPreview(
    onCameraReady: (LifecycleCameraController, LifecycleOwner) -> Unit,
    onCameraDisposed: () -> Unit,
    onPreviewStreaming: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val controller = remember { LifecycleCameraController(context) }
    val previewView = remember {
        PreviewView(context).apply {
            this.controller = controller
            scaleType = PreviewView.ScaleType.FILL_CENTER
        }
    }

    DisposableEffect(previewView, lifecycleOwner) {
        val observer = Observer<PreviewView.StreamState> { streamState ->
            if (streamState == PreviewView.StreamState.STREAMING) onPreviewStreaming()
        }
        previewView.previewStreamState.observe(lifecycleOwner, observer)
        onCameraReady(controller, lifecycleOwner)

        onDispose {
            previewView.previewStreamState.removeObserver(observer)
            onCameraDisposed()
        }
    }

    AndroidView(
        factory = { previewView },
        modifier = modifier
    )
}
