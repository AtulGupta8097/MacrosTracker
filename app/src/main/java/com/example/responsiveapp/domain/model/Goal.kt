package com.example.responsiveapp.domain.model

enum class Goal(
    val label: String,
    val description: String
) {
    LOSE("Lose Weight", "Fat loss focus"),
    MAINTAIN("Maintain Weight", "Maintain current weight"),
    GAIN("Gain Muscle", "Muscle building focus")
}