package com.example.responsiveapp.data.remote.dto.firebase

import com.example.responsiveapp.domain.model.MealIngredient
import com.example.responsiveapp.domain.model.NutritionInfo

data class FoodLogDto(

    val id: String = "",

    val date: Long = 0L,

    val foodName: String = "",

    val servingDescription: String = "",

    val quantity: Float = 1f,

    val nutrition: NutritionInfo = NutritionInfo(),

    val ingredients: List<MealIngredient> = emptyList(),

    val createdAt: Long = 0L,

    val updatedAt: Long = 0L
)