package com.example.responsiveapp.core.utils

object HealthMetricsUtils {

    fun calculateBmi(heightCm: Float, weightKg: Float): Float? {
        if (heightCm <= 0f || weightKg <= 0f) return null
        val heightM = heightCm / 100f
        return weightKg / (heightM * heightM)
    }

}
