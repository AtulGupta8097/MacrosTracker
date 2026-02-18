package com.example.responsiveapp.presentation.food_database_screen

data class FoodDatabaseState<T>(
    val data: List<T> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
)
