package com.example.responsiveapp.presentation.food_browse

import com.example.responsiveapp.domain.model.FoodDetail
import com.example.responsiveapp.domain.model.FoodItem


data class FoodBrowseState(
    val foods: List<FoodItem> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,

    val selectedFood: FoodDetail? = null,
    val isDetailLoading: Boolean = false,
    val detailError: String? = null,

    val query: String = "",
    val destination: FoodBrowseDestination = FoodBrowseDestination.List
)