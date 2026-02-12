package com.example.responsiveapp.domain.model

data class Serving(
    val id: String = "",
    val description: String = "", // "1 cup", "100g", "1 piece"
    val amount: Float = 0f,
    val unit: ServingUnit = ServingUnit.GRAM,
    val nutrition: NutritionInfo = NutritionInfo(),
    val isDefault: Boolean = false
)