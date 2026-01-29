package com.example.responsiveapp.domain.model
enum class ActivityLevel(
    val label: String,
    val description: String
) {
    SEDENTARY("Sedentary", "Little to no exercise"),
    LIGHT("Lightly Active", "1-3 days/week"),
    MODERATE("Moderately Active", "3-5 days/week"),
    ACTIVE("Very Active", "6-7 days/week"),
    VERY_ACTIVE("Extra Active", "Athlete/physical job")
}