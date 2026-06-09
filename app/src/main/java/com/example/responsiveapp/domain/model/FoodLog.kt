package com.example.responsiveapp.domain.model

data class FoodLog(
    val id: String,
    val userId: String,
    val date: Long = 0L,

    // Always present
    val foodName: String = "",
    val servingDescription: String = "",
    val quantity: Float = 1f,
    val nutrition: NutritionInfo = NutritionInfo(),

    // Only set for custom meals
    val ingredients: List<MealIngredient> = emptyList(),

    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
)