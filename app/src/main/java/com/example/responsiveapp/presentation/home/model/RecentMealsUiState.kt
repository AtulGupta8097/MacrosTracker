package com.example.responsiveapp.presentation.home.model

import androidx.compose.runtime.Immutable

@Immutable
data class FoodLogUiModel(
    val id: String,
    val name: String,
    val timeText: String,
    val caloriesText: String,
    val proteinText: String,
    val carbsText: String,
    val fatText: String,
)

@Immutable
data class RecentMealsUiState(
    val meals: List<FoodLogUiModel> = emptyList(),
) {
    val isEmpty: Boolean get() = meals.isEmpty()
}
