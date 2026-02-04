package com.example.responsiveapp.data.mapper

import com.example.responsiveapp.data.model.UserProfileDto
import com.example.responsiveapp.domain.model.ActivityLevel
import com.example.responsiveapp.domain.model.Gender
import com.example.responsiveapp.domain.model.Goal
import com.example.responsiveapp.domain.model.UserProfile

fun UserProfile.toDto(): UserProfileDto =
    UserProfileDto(
        gender = gender.name,
        age = age,
        height = height,
        weight = weight,
        activityLevel = activityLevel.name,
        goal = goal.name
    )

fun UserProfileDto.toDomain(): UserProfile? {
    if (
        gender == null || age == null || height == null ||
        weight == null || activityLevel == null || goal == null
    ) return null

    return UserProfile(
        gender = Gender.valueOf(gender),
        age = age,
        height = height,
        weight = weight,
        activityLevel = ActivityLevel.valueOf(activityLevel),
        goal = Goal.valueOf(goal)
    )
}
