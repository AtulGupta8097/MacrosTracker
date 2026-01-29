package com.example.responsiveapp.presentation.user_setup_screens

import com.example.responsiveapp.domain.model.ActivityLevel
import com.example.responsiveapp.domain.model.Gender
import com.example.responsiveapp.domain.model.Goal

data class UserInput(
    val gender: Gender? = null,
    val age: Int? = null,
    val weight: Float? = null,
    val height: Float? = null,
    val activityLevel: ActivityLevel? = null,
    val goal: Goal? = null
)