package com.example.responsiveapp.domain.model

data class FoodItem(
    val id: String,
    val name: String,
    val brand: String? = null,
    val foodType: String = "Generic",
    val macroSummary: MacroSummary
)

data class MacroSummary(
    val servingLabel: String,
    val calories: Float,
    val fat: Float,
    val carbs: Float,
    val protein: Float,
)