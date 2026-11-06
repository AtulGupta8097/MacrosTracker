package com.example.responsiveapp.core.camera

import kotlin.math.roundToInt

// CHANGED: new file. Snapshot of CameraX's *actual* zoom state (ratio + limits).
data class CameraZoom(
    val ratio: Float = 1f,
    val minRatio: Float = 1f,
    val maxRatio: Float = 1f,
) {
    // Pinch gestures emit many tiny changes (2.5012, 2.5031...). The UI shows one decimal, so
    // rounding here lets distinctUntilChanged() drop updates that would not change what is shown.
    fun roundedToTenth() = copy(
        ratio = ratio.toTenth(),
        minRatio = minRatio.toTenth(),
        maxRatio = maxRatio.toTenth(),
    )

    private fun Float.toTenth() = (this * 10f).roundToInt() / 10f
}
