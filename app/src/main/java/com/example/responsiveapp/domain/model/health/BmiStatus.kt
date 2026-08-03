package com.example.responsiveapp.domain.model.health

enum class BmiStatus(val label: String) {
    UNDERWEIGHT("Underweight"),
    HEALTHY("Healthy"),
    OVERWEIGHT("Overweight"),
    OBESE("Obese"),
    UNKNOWN("Unknown");

    companion object {
        fun fromBmi(bmi: Float?): BmiStatus {
            if (bmi == null || bmi <= 0f) return UNKNOWN
            return when {
                bmi < 18.5f -> UNDERWEIGHT
                bmi < 25f -> HEALTHY
                bmi < 30f -> OVERWEIGHT
                else -> OBESE
            }
        }
    }
}