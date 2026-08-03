package com.example.responsiveapp.presentation.home.mapper

import com.example.responsiveapp.domain.model.UserProfile
import com.example.responsiveapp.presentation.home.model.AppBarUiState
import com.example.responsiveapp.presentation.home.model.ProfileUiModel

fun UserProfile?.toAppBarUiState(): AppBarUiState {

    if (this == null) {
        return AppBarUiState()
    }

    return AppBarUiState(
        avatarInitial = avatarInitialOf(name),
        profile = ProfileUiModel(
            name = name.ifBlank { "Your Profile" },
            avatarInitial = avatarInitialOf(name),
            ageText = "$age years",
            heightText = "${height.toInt()} cm",
            weightText = "$weight kg",
            targetWeightText = "$targetWeight kg",
            activityLabel = activityLevel.label,
            goalLabel = goal.label,
        )
    )
}

private fun avatarInitialOf(name: String): String =
    name.trim()
        .firstOrNull()
        ?.uppercaseChar()
        ?.toString()
        ?: "?"
