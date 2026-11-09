package com.macrivo.app.domain.model.mymeals

import com.macrivo.app.domain.model.MealIngredient
import com.macrivo.app.domain.model.NutritionInfo

data class MyMeal(
    val id: String,
    val name: String,
    val ingredients: List<MealIngredient>,
    val totalNutritionInfo: NutritionInfo,
    val createAt: Long,
    val updatedAt: Long,
)
