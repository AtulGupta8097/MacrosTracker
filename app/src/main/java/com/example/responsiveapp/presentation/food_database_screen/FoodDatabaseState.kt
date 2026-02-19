package com.example.responsiveapp.presentation.food_database_screen

import com.example.responsiveapp.domain.model.Food

data class FoodDatabaseState<T>(
    val data: List<T> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val foodDetailData: Food? = null,
    val foodDetailLoading: Boolean = false,
    val foodDetailError: String? = null
)
