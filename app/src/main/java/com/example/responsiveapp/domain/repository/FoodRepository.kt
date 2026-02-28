package com.example.responsiveapp.domain.repository

import com.example.responsiveapp.domain.model.FoodDetail
import com.example.responsiveapp.domain.model.FoodItem

// domain/repository/FoodRepository.kt
interface FoodRepository {
    suspend fun searchFoods(query: String, limit: Int = 20): Result<List<FoodItem>>
    suspend fun getFoodDetail(foodId: String): Result<FoodDetail>
}