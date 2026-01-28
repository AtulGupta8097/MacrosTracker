package com.example.responsiveapp.domain.model

data class UserData(
    val gender: Gender? = null,
    val age: Int? = null,
    val weight: Float? = null,
    val height: Float? = null,
    val activityLevel: ActivityLevel? = null,
    val goal: Goal? = null
)