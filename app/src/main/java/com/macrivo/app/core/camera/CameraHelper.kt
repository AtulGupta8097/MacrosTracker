package com.macrivo.app.core.camera

import androidx.camera.core.ImageAnalysis
import androidx.camera.view.LifecycleCameraController
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.flow.StateFlow
import java.util.concurrent.Executor

interface CameraHelper {

    // CHANGED: the camera's real zoom (pinch gestures, setZoomRatio, lens switches all land here).
    // Observed from CameraX's ZoomState; the UI must show this, not the last requested value.
    val zoom: StateFlow<CameraZoom>

    fun initialize(
        controller: LifecycleCameraController,
        lifecycleOwner: LifecycleOwner,
        onError: (Throwable) -> Unit = {}
    )

    fun setAnalyzer(
        executor: Executor,
        analyzer: ImageAnalysis.Analyzer
    )

    fun clearAnalyzer()

    fun getCameraCapabilities(): CameraCapabilities

    fun setCameraLens(isFrontCamera: Boolean)

    fun setZoomRatio(ratio: Float)

    fun setTorch(enabled: Boolean)

    fun release()
}
