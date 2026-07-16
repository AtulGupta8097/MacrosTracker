package com.example.responsiveapp.data.remote.dto.firebase

import com.example.responsiveapp.domain.model.NutritionTargets

data class MacroTargetDto(

    val id: String = "",

    val targets: NutritionTargets = NutritionTargets(),

    val bmr: Int = 0,

    val tdee: Int = 0,

    val createdAt: Long = 0L,

    val updatedAt: Long = 0L
)