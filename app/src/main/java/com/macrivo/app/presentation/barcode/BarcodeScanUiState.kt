package com.macrivo.app.presentation.barcode

import androidx.compose.runtime.Immutable
import com.macrivo.app.domain.model.FoodDetail
import com.macrivo.app.domain.model.NutritionInfo
import com.macrivo.app.domain.model.Serving
import com.macrivo.app.domain.model.scale

@Immutable
data class BarcodeScanUiState(
    val status: BarcodeScanStatus = BarcodeScanStatus.INITIALIZING,

    val hasFlash: Boolean = false,
    val isTorchEnabled: Boolean = false,
    val isFrontCamera: Boolean = false,
    val zoomRatio: Float = 1f,
    val minZoomRatio: Float = 1f,
    val maxZoomRatio: Float = 1f,

    val scannedBarcode: String? = null,
    val food: FoodDetail? = null,
    val selectedServing: Serving? = null,
    val quantity: Float = 1f,
    val errorMessage: String? = null
) {
    val scaledNutrition: NutritionInfo?
        get() = selectedServing?.nutrition?.scale(quantity)
}
