package com.macrivo.app.core.camera

import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.view.LifecycleCameraController
import androidx.camera.core.ZoomState
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.concurrent.CancellationException
import java.util.concurrent.Executor

class CameraHelperImpl : CameraHelper {

    private var cameraController: LifecycleCameraController? = null

    // CHANGED: actual zoom state, fed by CameraX (see observeZoom).
    private val _zoom = MutableStateFlow(CameraZoom())
    override val zoom: StateFlow<CameraZoom> = _zoom.asStateFlow()

    private var zoomLiveData: LiveData<ZoomState>? = null
    private var zoomObserver: Observer<ZoomState>? = null

    override fun initialize(
        controller: LifecycleCameraController,
        lifecycleOwner: LifecycleOwner,
        onError: (Throwable) -> Unit
    ) {
        if (cameraController != null) return

        try {
            cameraController = controller.apply {
                setEnabledUseCases(
                    LifecycleCameraController.IMAGE_ANALYSIS
                )

                bindToLifecycle(lifecycleOwner)
            }
        } catch (e: Exception) {
            cameraController = null
            onError(e)
            return
        }

        observeZoom(controller, lifecycleOwner)

        val future = controller.initializationFuture
        future.addListener(
            {
                try {
                    future.get()
                } catch (e: CancellationException) {
                } catch (e: Exception) {
                    onError(e)
                }
            },
            Runnable::run
        )
    }

    override fun setAnalyzer(
        executor: Executor,
        analyzer: ImageAnalysis.Analyzer
    ) {
        cameraController?.setImageAnalysisAnalyzer(
            executor,
            analyzer
        )
    }

    override fun clearAnalyzer() {
        cameraController?.clearImageAnalysisAnalyzer()
    }

    override fun getCameraCapabilities(): CameraCapabilities {
        val cameraInfo = cameraController?.cameraInfo
            ?: return CameraCapabilities()

        val zoomState = cameraInfo.zoomState.value

        return CameraCapabilities(
            hasFlash = cameraInfo.hasFlashUnit(),
            minZoomRatio = zoomState?.minZoomRatio ?: 1f,
            maxZoomRatio = zoomState?.maxZoomRatio ?: 1f
        )
    }

    override fun setCameraLens(isFrontCamera: Boolean) {
        val controller = cameraController ?: return

        controller.cameraSelector = if (isFrontCamera) {
            CameraSelector.DEFAULT_FRONT_CAMERA
        } else {
            CameraSelector.DEFAULT_BACK_CAMERA
        }
    }

    override fun setZoomRatio(ratio: Float) {
        val controller = cameraController ?: return
        val zoomState = controller.cameraInfo?.zoomState?.value ?: return

        val validRatio = ratio.coerceIn(
            zoomState.minZoomRatio,
            zoomState.maxZoomRatio
        )

        controller.setZoomRatio(validRatio)
    }

    override fun setTorch(enabled: Boolean) {
        val controller = cameraController ?: return

        if (controller.cameraInfo?.hasFlashUnit() ?: false) {
            controller.enableTorch(enabled)
        }
    }

    // CHANGED: ZoomState is a LiveData, so observe() is lifecycle-aware (auto-removed when the
    // owner is destroyed) and is also removed explicitly in release().
    // controller.zoomState is the controller-level view of cameraInfo.zoomState: it is usable
    // before the camera is bound and keeps emitting after a lens switch, when cameraInfo changes.
    private fun observeZoom(controller: LifecycleCameraController, owner: LifecycleOwner) {
        stopObservingZoom()
        val liveData = controller.zoomState
        val observer = Observer<ZoomState> { state ->
            val next = CameraZoom(
                ratio = state.zoomRatio,
                minRatio = state.minZoomRatio,
                maxRatio = state.maxZoomRatio,
            )
            // StateFlow ignores equal values, so unchanged pinch frames don't propagate.
            _zoom.value = next
        }
        liveData.observe(owner, observer)
        zoomLiveData = liveData
        zoomObserver = observer
    }

    private fun stopObservingZoom() {
        val liveData = zoomLiveData
        val observer = zoomObserver
        if (liveData != null && observer != null) liveData.removeObserver(observer)
        zoomLiveData = null
        zoomObserver = null
    }

    override fun release() {
        stopObservingZoom()
        cameraController?.apply {
            clearImageAnalysisAnalyzer()
            unbind()
        }

        cameraController = null
    }
}
