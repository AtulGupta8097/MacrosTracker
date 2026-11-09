package com.macrivo.app.data.remote.dto.firebase

import com.macrivo.app.domain.model.MealIngredient
import com.macrivo.app.domain.model.NutritionInfo

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