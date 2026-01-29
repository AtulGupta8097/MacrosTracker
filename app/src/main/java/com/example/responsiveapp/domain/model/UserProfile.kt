package com.example.responsiveapp.domain.model

data class UserProfile(
    val gender: Gender,
    val age: Int,
    val weight: Float,
    val height: Float,
    val activityLevel: ActivityLevel,
    val goal: Goal
)