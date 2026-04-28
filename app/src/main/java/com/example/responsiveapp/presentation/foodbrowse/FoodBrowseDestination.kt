package com.example.responsiveapp.presentation.foodbrowse

sealed interface FoodBrowseDestination {
    data object List : FoodBrowseDestination
    data class Detail(val foodId: String) : FoodBrowseDestination
}