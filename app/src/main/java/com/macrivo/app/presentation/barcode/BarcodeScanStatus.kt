package com.macrivo.app.presentation.barcode

enum class BarcodeScanStatus {
    INITIALIZING,
    SCANNING,
    FETCHING_PRODUCT,
    PRODUCT_FOUND,
    FOOD_DETAILS,
    LOGGING,
    NOT_FOUND,
    ERROR,
    ADDED,
    CAMERA_UNAVAILABLE
}
