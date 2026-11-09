package com.macrivo.app.presentation.myfood

sealed interface MyFoodDestination {

    data object List : MyFoodDestination
    data object Create : MyFoodDestination
    data class Edit(val foodId: String) : MyFoodDestination
}