package com.example.responsiveapp.presentation.settings.model

import androidx.compose.runtime.Immutable
import com.example.responsiveapp.domain.model.ActivityLevel
import com.example.responsiveapp.domain.model.Gender
import com.example.responsiveapp.domain.model.Goal
import com.example.responsiveapp.domain.model.ThemeMode

@Immutable
data class SettingsUiState(
    val isLoading: Boolean = true,
    val profile: ProfileSummaryUiState = ProfileSummaryUiState(),
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
)

@Immutable
data class ProfileSummaryUiState(
    val name: String = "",
    val avatarInitial: String = "?",
    val email: String = "",
    val currentWeightText: String = "—",
    val targetWeightText: String = "—",
    val bodyDetailsText: String = "",
    val goal: Goal = Goal.MAINTAIN,
    val activityLevel: ActivityLevel = ActivityLevel.MODERATE,
    val gender: Gender = Gender.MALE,
    val rawWeight: Float = 0f,
    val rawTargetWeight: Float = 0f,
    val rawHeight: Float = 0f,
    val rawAge: Int = 0,
)
