package com.macrivo.app.presentation.foodbrowse.component

sealed interface FoodBrowseEvent {
    data object FoodLogged : FoodBrowseEvent
}