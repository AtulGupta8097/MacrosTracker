package com.example.responsiveapp.presentation.foodbrowse.component

sealed interface FoodBrowseEvent {
    data object FoodLogged : FoodBrowseEvent
}