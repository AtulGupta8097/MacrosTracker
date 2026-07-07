package com.example.responsiveapp.domain.model

data class DailySummary(
    val date: Long,
    val target: NutritionTargets,
    val consumed: NutritionProgress,
    val createdAt: Long,
    val updatedAt: Long
)