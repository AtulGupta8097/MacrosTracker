package com.example.responsiveapp.presentation.myfood

sealed interface MyFoodDestination {

    data object List : MyFoodDestination
    data object Create : MyFoodDestination
    data class Edit(val foodId: String) : MyFoodDestination
}