package com.example.responsiveapp.domain.model

enum class LogMethod(val displayName: String) {
    DATABASE("Food Database"),
    BARCODE("Scan Barcode"),
    CAMERA("Scan Food"),
    SAVED("Saved Foods"),
    MANUAL("Manual Entry")
}