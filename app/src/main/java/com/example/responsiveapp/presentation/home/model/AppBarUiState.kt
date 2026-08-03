package com.example.responsiveapp.presentation.home.model

import androidx.compose.runtime.Immutable

@Immutable
data class AppBarUiState(
    val avatarInitial: String = "?",
    val profile: ProfileUiModel? = null,
)
