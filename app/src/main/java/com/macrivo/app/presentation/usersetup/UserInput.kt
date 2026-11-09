package com.macrivo.app.presentation.usersetup

import com.macrivo.app.domain.model.ActivityLevel
import com.macrivo.app.domain.model.Gender
import com.macrivo.app.domain.model.Goal
import com.macrivo.app.domain.model.UserProfile

data class UserInput(
    val name: String? = null,
    val gender: Gender? = null,
    val age: Int? = null,
    val weight: Float? = null,
    val height: Float? = null,
    val activityLevel: ActivityLevel? = null,
    val targetWeight: Float? = null,
    val goal: Goal? = null
)

fun UserInput.toDomain(): UserProfile {
    return UserProfile(
        name = requireNotNull(name).trim(),
        gender = requireNotNull(gender),
        age = requireNotNull(age),
        height = requireNotNull(height),
        weight = requireNotNull(weight),
        activityLevel = requireNotNull(activityLevel),
        targetWeight = requireNotNull(targetWeight),
        goal = requireNotNull(goal)
    )
}
