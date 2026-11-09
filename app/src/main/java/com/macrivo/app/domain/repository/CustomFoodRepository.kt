package com.macrivo.app.domain.repository

import com.macrivo.app.domain.model.myfood.CustomFood
import kotlinx.coroutines.flow.Flow

interface CustomFoodRepository {

    fun observeAll(): Flow<List<CustomFood>>

    suspend fun save(food: CustomFood)

    suspend fun delete(foodId: String)

    suspend fun syncPending()

    suspend fun fetchAndCacheAll()
}