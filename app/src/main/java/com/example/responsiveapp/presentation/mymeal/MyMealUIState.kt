package com.example.responsiveapp.presentation.mymeal

import com.example.responsiveapp.domain.model.FoodItem
import com.example.responsiveapp.domain.model.MealIngredient
import com.example.responsiveapp.domain.model.mymeals.MyMeal

data class MyMealUIState(
    val meals: List<MyMeal> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val ingredient: Map<String, MealIngredient> = emptyMap(),
    val showCreateSheet: Boolean = false,
    val destination: MyMealDestination = MyMealDestination.MyMealList,

    // FoodBottomSheetUI
    val sheetSearchQuery: String= "",
    val sheetFoods: List<FoodItem> = emptyList(),
    val isFoodListLoading: Boolean= false,
    val sheetErrorMessage: String? = null,
)