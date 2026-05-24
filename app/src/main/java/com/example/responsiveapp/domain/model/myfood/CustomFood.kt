package com.example.responsiveapp.domain.model.myfood

import com.example.responsiveapp.domain.model.NutritionInfo

data class CustomFood(

    val id: String,

    // Basic Info
    val name: String,
    val description: String,
    val servingSize: String,
    val servingsPerContainer: Float,

    // Nutrition
    val nutrition: NutritionInfo,

    val createdAt: Long
)