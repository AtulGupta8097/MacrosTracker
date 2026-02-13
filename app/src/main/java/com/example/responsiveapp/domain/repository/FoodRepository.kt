package com.example.responsiveapp.domain.repository

import com.example.responsiveapp.domain.model.Food

interface FoodRepository {
    suspend fun searchFoods(
        query: String,
        limit: Int = 20
    ): Result<List<Food>>

    suspend fun getFoodById(foodId: String): Result<Food>
}