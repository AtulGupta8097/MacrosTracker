package com.example.responsiveapp.presentation.home.model

import androidx.compose.runtime.Immutable

@Immutable
data class ProfileUiModel(
    val name: String,
    val avatarInitial: String,
    val ageText: String,
    val heightText: String,
    val weightText: String,
    val targetWeightText: String,
    val activityLabel: String,
    val goalLabel: String,
)
