package com.example.responsiveapp.data.remote.dto.firebase

import com.example.responsiveapp.domain.model.NutritionInfo

data class CustomFoodDto(

    val id: String = "",

    val name: String = "",

    val description: String = "",

    val servingSize: String = "",

    val servingsPerContainer: Float = 0f,

    val nutrition: NutritionInfo = NutritionInfo(),

    val createdAt: Long = 0L,

    val updatedAt: Long = 0L
)