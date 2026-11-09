package com.macrivo.app.domain.model.foodlog

import com.macrivo.app.domain.model.MealIngredient
import com.macrivo.app.domain.model.NutritionInfo

data class LogFoodRequest(

    val foodName: String,

    val servingDescription: String,

    val quantity: Float,

    val nutrition: NutritionInfo,

    val ingredients: List<MealIngredient> = emptyList()

)