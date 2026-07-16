package com.example.responsiveapp.domain.model.mymeals

import com.example.responsiveapp.domain.model.MealIngredient
import com.example.responsiveapp.domain.model.NutritionInfo

data class MyMeal(
    val id: String,
    val name: String,
    val ingredients: List<MealIngredient>,
    val totalNutritionInfo: NutritionInfo,
    val createAt: Long,
    val updatedAt: Long,
)
