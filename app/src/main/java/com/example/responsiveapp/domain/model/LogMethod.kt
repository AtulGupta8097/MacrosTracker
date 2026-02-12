package com.example.responsiveapp.domain.model

enum class LogMethod(val displayName: String) {
    DATABASE("Food Database"),      // Search from Firebase database
    BARCODE("Scan Barcode"),        // Barcode scanner
    CAMERA("Scan Food"),            // AI camera recognition
    SAVED("Saved Foods"),           // Quick add from favorites
    MANUAL("Manual Entry")          // Custom manual entry
}