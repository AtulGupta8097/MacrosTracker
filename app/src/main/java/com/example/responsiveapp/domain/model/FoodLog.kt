package com.example.responsiveapp.domain.model

data class FoodLog(
    val id: String,
    val userId: String,
    val date: Long = 0L,
    val logSource: LogMethod = LogMethod.DATABASE,
    // Nullable: only set when logged from FatSecret/barcode
    val foodId: String? = null,
    val brand: String? = null,
    val servingId: String? = null,
    // Always present
    val foodName: String = "",
    val servingDescription: String = "",
    val quantity: Float = 1f,
    val nutrition: NutritionInfo = NutritionInfo(),
    // Only set for custom meals
    val ingredients: List<MealIngredient> = emptyList(),
    // Common optional
    val notes: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val isSynced: Boolean = false
)