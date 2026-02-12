package com.example.responsiveapp.domain.model

data class FoodLog(
    val id: String = "",
    val userId: String = "",
    val foodId: String = "",
    val foodName: String = "",
    val brand: String? = null,
    val servingId: String = "",
    val servingDescription: String = "",
    val quantity: Float = 1f,
    val nutrition: NutritionInfo = NutritionInfo(),
    val mealType: MealType = MealType.BREAKFAST,
    val date: Long = 0L, 
    val logMethod: LogMethod = LogMethod.DATABASE,
    val notes: String? = null,
    val imageUrl: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val isSynced: Boolean = false
)