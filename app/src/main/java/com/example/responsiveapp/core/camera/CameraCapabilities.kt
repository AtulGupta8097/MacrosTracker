package com.example.responsiveapp.core.camera

data class CameraCapabilities(
    val hasFlash: Boolean = false,
    val minZoomRatio: Float = 1f,
    val maxZoomRatio: Float = 1f
)