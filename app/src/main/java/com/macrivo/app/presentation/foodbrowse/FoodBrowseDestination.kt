package com.macrivo.app.presentation.foodbrowse

import androidx.compose.runtime.Immutable

@Immutable
sealed interface FoodBrowseDestination {
    data object List : FoodBrowseDestination
    data class Detail(val foodId: String) : FoodBrowseDestination
}