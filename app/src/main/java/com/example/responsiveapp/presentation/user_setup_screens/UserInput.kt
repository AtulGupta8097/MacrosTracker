package com.example.responsiveapp.presentation.user_setup_screens

import com.example.responsiveapp.domain.model.ActivityLevel
import com.example.responsiveapp.domain.model.Gender
import com.example.responsiveapp.domain.model.Goal
import com.example.responsiveapp.domain.model.UserProfile

data class UserInput(
    val gender: Gender? = null,
    val age: Int? = null,
    val weight: Float? = null,
    val height: Float? = null,
    val activityLevel: ActivityLevel? = null,
    val goal: Goal? = null
)

fun UserInput.toDomain(): UserProfile {
    return UserProfile(
        gender = requireNotNull(gender),
        age = requireNotNull(age),
        height = requireNotNull(height),
        weight = requireNotNull(weight),
        activityLevel = requireNotNull(activityLevel),
        goal = requireNotNull(goal)
    )
}
