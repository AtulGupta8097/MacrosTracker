package com.example.responsiveapp.domain.repository

import com.example.responsiveapp.domain.model.FoodDetail
import kotlinx.coroutines.flow.Flow

interface SavedFoodRepository {

    fun observeAll(): Flow<List<FoodDetail>>

    fun observeIsSaved(foodId: String): Flow<Boolean>

    suspend fun save(food: FoodDetail)

    suspend fun remove(foodId: String)

    suspend fun syncPending()

    suspend fun fetchAndCacheAll()
}
