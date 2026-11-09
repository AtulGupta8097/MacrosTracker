package com.macrivo.app.core.camera

data class CameraCapabilities(
    val hasFlash: Boolean = false,
    val minZoomRatio: Float = 1f,
    val maxZoomRatio: Float = 1f
)