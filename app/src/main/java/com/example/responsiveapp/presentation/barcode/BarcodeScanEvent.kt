package com.example.responsiveapp.presentation.barcode

sealed interface BarcodeScanEvent {
    data class ShowMessage(val message: String) : BarcodeScanEvent
    data object NavigateBack : BarcodeScanEvent
    data object Logged : BarcodeScanEvent
}
