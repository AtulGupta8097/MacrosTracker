package com.macrivo.app.data.remote.dto.firebase

import com.macrivo.app.domain.model.NutritionProgress
import com.macrivo.app.domain.model.NutritionTargets

data class DailySummaryDto(

    val date: Long = 0L,

    val target: NutritionTargets = NutritionTargets(),

    val consumed: NutritionProgress = NutritionProgress(),

    val createdAt: Long = 0L,

    val updatedAt: Long = 0L
)