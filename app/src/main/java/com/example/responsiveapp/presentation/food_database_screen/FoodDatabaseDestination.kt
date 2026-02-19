package com.example.responsiveapp.presentation.food_database_screen

sealed class FoodDatabaseDestination {
    data object FoodList : FoodDatabaseDestination()
    data class FoodDetail(val foodId: String) : FoodDatabaseDestination()
}

sealed class MyMealsDestination {
    data object MealList : MyMealsDestination()
    data class MealDetail(val mealId: String) : MyMealsDestination()
    data object CreateMeal : MyMealsDestination()
}