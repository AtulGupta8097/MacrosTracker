package com.example.responsiveapp.domain.model.foodlog

import com.example.responsiveapp.domain.model.MealIngredient
import com.example.responsiveapp.domain.model.NutritionInfo

data class LogFoodRequest(

    val foodName: String,

    val servingDescription: String,

    val quantity: Float,

    val nutrition: NutritionInfo,

    val ingredients: List<MealIngredient> = emptyList()

)