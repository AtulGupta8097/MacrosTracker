package com.macrivo.app.data.remote.dto.firebase

import com.macrivo.app.domain.model.NutritionInfo

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