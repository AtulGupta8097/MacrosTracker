package com.example.responsiveapp.domain.model.macros

data class CurrentMacroTarget(
    val id: String,
    val userId: String,
    val dailyCalories: Int,
    val dailyProtein: Float,
    val dailyCarbs: Float,
    val dailyFats: Float,
    val createdAt: Long,
    val updatedAt: Long
)