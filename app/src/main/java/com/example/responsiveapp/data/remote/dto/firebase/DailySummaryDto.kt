package com.example.responsiveapp.data.remote.dto.firebase

import com.example.responsiveapp.domain.model.NutritionProgress
import com.example.responsiveapp.domain.model.NutritionTargets

data class DailySummaryDto(

    val date: Long = 0L,

    val target: NutritionTargets = NutritionTargets(),

    val consumed: NutritionProgress = NutritionProgress(),

    val createdAt: Long = 0L,

    val updatedAt: Long = 0L
)