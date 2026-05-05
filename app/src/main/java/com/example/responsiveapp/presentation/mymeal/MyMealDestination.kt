package com.example.responsiveapp.presentation.mymeal

sealed interface MyMealDestination {
    data object MyMealList: MyMealDestination
    data object Create: MyMealDestination
    data class Edit(val mealId: String): MyMealDestination
}