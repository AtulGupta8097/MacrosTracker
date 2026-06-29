package com.example.responsiveapp.domain.model

data class UserProfile(
    val age: Int,
    val gender: Gender,
    val height: Float,
    val weight: Float,
    val targetWeight: Float = 62.8f,
    val activityLevel: ActivityLevel,
    val goal: Goal
)