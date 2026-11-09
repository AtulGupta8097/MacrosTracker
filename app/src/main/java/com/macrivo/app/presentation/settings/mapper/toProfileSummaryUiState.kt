package com.macrivo.app.presentation.settings.mapper

import com.macrivo.app.core.utils.avatarInitial
import com.macrivo.app.domain.model.UserProfile
import com.macrivo.app.presentation.settings.model.ProfileSummaryUiState

fun UserProfile?.toProfileSummaryUiState(email: String?): ProfileSummaryUiState {

    if (this == null) {
        return ProfileSummaryUiState(email = email.orEmpty())
    }

    return ProfileSummaryUiState(
        name = name.ifBlank { "Your Profile" },
        avatarInitial = name.avatarInitial(),
        email = email.orEmpty(),
        currentWeightText = "%.1f kg".format(weight),
        targetWeightText = "%.1f kg".format(targetWeight),
        bodyDetailsText = "${height.toInt()}cm · $age · ${gender.label}",
        goal = goal,
        activityLevel = activityLevel,
        gender = gender,
        rawWeight = weight,
        rawTargetWeight = targetWeight,
        rawHeight = height,
        rawAge = age,
    )
}
