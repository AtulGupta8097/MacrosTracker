package com.example.responsiveapp.presentation.food_browse

sealed interface FoodBrowseDestination {
    data object List : FoodBrowseDestination
    data class Detail(val foodId: String) : FoodBrowseDestination
}