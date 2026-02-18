package com.example.responsiveapp.domain.model

data class MealIngredient(
    val foodId: String? = null,
    val foodName: String,
    val brand: String? = null,
    val servingId: String? = null,
    val servingDescription: String = "",
    val quantity: Float = 1f,
    val nutrition: NutritionInfo = NutritionInfo(),
)