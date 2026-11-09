package com.macrivo.app.data.remote.dto.firebase

import com.macrivo.app.domain.model.NutritionTargets

data class MacroTargetDto(

    val id: String = "",

    val targets: NutritionTargets = NutritionTargets(),

    val bmr: Int = 0,

    val tdee: Int = 0,

    val bmi: Float = 0f,

    val createdAt: Long = 0L,

    val updatedAt: Long = 0L
)