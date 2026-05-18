package com.example.responsiveapp.presentation.mymeal

import com.example.responsiveapp.domain.model.CustomToastProperty
import com.example.responsiveapp.domain.model.FoodItem
import com.example.responsiveapp.domain.model.MealIngredient
import com.example.responsiveapp.domain.model.mymeals.MyMeal
import com.example.responsiveapp.presentation.commoncomponent.SuccessToast

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
    val showToast: Boolean = false,
    val toastType: CustomToastProperty = SuccessToast(),
    val toastMessage: String? = null,
    val toastDuration: Long = 3000,

    // FoodBottomSheetUI
    val sheetSearchQuery: String= "",
    val sheetFoods: List<FoodItem> = emptyList(),
    val isFoodListLoading: Boolean= false,
    val sheetErrorMessage: String? = null,
)