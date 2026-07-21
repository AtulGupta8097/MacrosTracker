package com.example.responsiveapp.domain.repository

import com.example.responsiveapp.domain.model.foodlog.FoodLog

interface FoodLogRepository {

    suspend fun logFood(foodLog: FoodLog)

    suspend fun syncPending()

    suspend fun fetchAndCacheAll()
}