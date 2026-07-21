package com.example.responsiveapp.domain.repository

import com.example.responsiveapp.domain.model.myfood.CustomFood
import kotlinx.coroutines.flow.Flow

interface CustomFoodRepository {

    fun observeAll(): Flow<List<CustomFood>>

    suspend fun save(food: CustomFood)

    suspend fun delete(foodId: String)

    suspend fun syncPending()

    suspend fun fetchAndCacheAll()
}