package com.example.responsiveapp.domain.model

data class UserProfile(
    val id: String = "",
    val name: String = "",
    val gender: Gender,
    val age: Int,
    val height: Float,
    val weight: Float,
    val targetWeight: Float = 62.8f,
    val activityLevel: ActivityLevel,
    val goal: Goal,
    val updatedAt: Long = System.currentTimeMillis()
)