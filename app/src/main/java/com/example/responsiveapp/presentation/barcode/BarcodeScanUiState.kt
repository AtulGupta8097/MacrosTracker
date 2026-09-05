package com.example.responsiveapp.presentation.barcode

import androidx.compose.runtime.Immutable
import com.example.responsiveapp.domain.model.NutritionInfo
import com.example.responsiveapp.domain.model.Serving

sealed class BarcodeScanUiState {

    data object Scanning : BarcodeScanUiState()

    data object Loading : BarcodeScanUiState()

    data object NotFound : BarcodeScanUiState()

    data class Error(val message: String) : BarcodeScanUiState()

    data object Added : BarcodeScanUiState()

    @Immutable
    data class Found(
        val foodName: String,
        val foodBrand: String?,
        val servings: List<Serving>,
        val selectedServing: Serving,
        val quantity: Float,
        val scaledNutrition: NutritionInfo,
        val isAdding: Boolean = false,
    ) : BarcodeScanUiState()
}