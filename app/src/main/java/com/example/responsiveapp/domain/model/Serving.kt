package com.example.responsiveapp.domain.model


data class Serving(
    val id: String,
    val description: String,        // "1 egg", "100g", "14 pieces"
    val metricAmount: Float,        // always in metric (g or ml)
    val metricUnit: ServingUnit,
    val numberOfUnits: Float,       // e.g. 14.0 for "14 pieces"
    val measurementDescription: String, // e.g. "pieces", "serving"
    val nutrition: NutritionInfo,
    val isDefault: Boolean = false,
)
