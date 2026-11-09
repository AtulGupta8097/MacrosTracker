package com.macrivo.app.presentation.home.mapper

import com.macrivo.app.core.utils.avatarInitial
import com.macrivo.app.domain.model.UserProfile
import com.macrivo.app.presentation.home.model.AppBarUiState
import com.macrivo.app.presentation.home.model.ProfileUiModel

fun UserProfile?.toAppBarUiState(): AppBarUiState {

    if (this == null) {
        return AppBarUiState()
    }

    return AppBarUiState(
        avatarInitial = name.avatarInitial(),
        profile = ProfileUiModel(
            name = name.ifBlank { "Your Profile" },
            avatarInitial = name.avatarInitial(),
            ageText = "$age years",
            heightText = "${height.toInt()} cm",
            weightText = "$weight kg",
            targetWeightText = "$targetWeight kg",
            activityLabel = activityLevel.label,
            goalLabel = goal.label,
        )
    )
}

