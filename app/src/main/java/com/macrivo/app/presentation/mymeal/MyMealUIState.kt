package com.macrivo.app.presentation.mymeal

import com.macrivo.app.domain.calculator.ToastState
import com.macrivo.app.domain.model.FoodItem
import com.macrivo.app.domain.model.MealIngredient
import com.macrivo.app.domain.model.mymeals.MyMeal

data class MyMealUIState(
    val meals: List<MyMeal> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val mealName: String = "",
    val ingredient: Map<String, MealIngredient> = emptyMap(),
    val editingMeal: MyMeal? = null,
    val hasUnsavedChanges : Boolean = false,
    val showCreateSheet: Boolean = false,
    val destination: MyMealDestination = MyMealDestination.MyMealList,

    // ToastUI
    val toast: ToastState = ToastState(),

    // FoodBottomSheetUI
    val sheetSearchQuery: String= "",
    val sheetFoods: List<FoodItem> = emptyList(),
    val isFoodListLoading: Boolean= false,
    val sheetErrorMessage: String? = null,
)