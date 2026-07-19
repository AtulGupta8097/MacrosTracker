package com.example.responsiveapp.presentation.foodbrowse

import androidx.compose.runtime.Immutable
import com.example.responsiveapp.domain.calculator.ToastState
import com.example.responsiveapp.domain.model.FoodDetail
import com.example.responsiveapp.domain.model.FoodItem
import com.example.responsiveapp.domain.model.Serving

@Immutable
data class FoodBrowseState(
    val foods: List<FoodItem> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val query: String = "",
    val isLogging: Boolean = false,
    val toast: ToastState = ToastState(),
    val destination: FoodBrowseDestination = FoodBrowseDestination.List,
    val selectedFood: FoodDetail? = null,
    val isDetailLoading: Boolean = false,
    val detailError: String? = null,
    val selectedServing: Serving? = null,
    val quantity: Float = 1f,
    val isManuallySelected: Boolean = false,
)