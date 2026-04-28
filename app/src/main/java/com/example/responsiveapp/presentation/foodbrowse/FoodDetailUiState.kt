package com.example.responsiveapp.presentation.foodbrowse

import androidx.compose.runtime.Immutable
import com.example.responsiveapp.domain.model.NutritionInfo
import com.example.responsiveapp.domain.model.Serving

sealed class FoodDetailUiState {

    data object Loading : FoodDetailUiState()

    data class Error(val message: String) : FoodDetailUiState()

    @Immutable
    data class Success(
        val foodName: String,
        val foodBrand: String?,
        val servings: List<Serving>,
        val selectedServing: Serving,
        val quantity: Float,
        val scaledNutrition: NutritionInfo,
    ) : FoodDetailUiState()
}