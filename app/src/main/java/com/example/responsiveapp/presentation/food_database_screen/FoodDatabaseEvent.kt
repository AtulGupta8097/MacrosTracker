package com.example.responsiveapp.presentation.food_database_screen

sealed class FoodDatabaseEvent {
    data class SearchQueryChanged(val query: String) : FoodDatabaseEvent()
    data class SearchTriggered(val query: String) : FoodDatabaseEvent()
    data object ClearSearch : FoodDatabaseEvent()
}