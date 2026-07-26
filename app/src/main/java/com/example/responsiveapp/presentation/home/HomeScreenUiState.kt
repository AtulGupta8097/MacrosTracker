package com.example.responsiveapp.presentation.home

import androidx.compose.runtime.Immutable
import com.example.responsiveapp.domain.model.UserProfile

@Immutable
data class HomeUiState(
    val userProfile: UserProfile? = null,
    val isLoading: Boolean = true,
)
